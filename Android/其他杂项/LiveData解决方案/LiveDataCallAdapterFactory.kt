import androidx.lifecycle.LiveData
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val responseType: Type
        if (getRawType(returnType) != LiveData::class.javaObjectType) {
            throw IllegalStateException("return type must be parameterized")
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        responseType = if (rawObservableType == Response::class.javaObjectType) {
            if (observableType !is ParameterizedType) {
                throw IllegalArgumentException("Response must be parameterized")
            }
            getParameterUpperBound(0, observableType)
        } else {
            observableType
        }
        return LiveDataCallAdapter<Any>(responseType)
    }

    class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<R>> {
        override fun responseType() = responseType
        override fun adapt(call: Call<R>): LiveData<R> {
            return object : LiveData<R>() {
                private var started = AtomicBoolean(false)
                override fun onActive() {
                    super.onActive()
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : Callback<R> {
                            override fun onResponse(call: Call<R>, response: Response<R>) {
                                postValue(response.body())
                            }

                            override fun onFailure(call: Call<R>, throwable: Throwable) {
                                postValue(null)
                            }
                        })
                    }
                }
            }
        }
    }

}

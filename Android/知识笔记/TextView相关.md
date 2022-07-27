```java
    /**
     * EditText限制只能输入汉字
     */
    public InputFilter getInputFilter() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                    Spanned dest, int dstart, int dend) {
                if (TextUtils.isEmpty(source)){
                    return "";
                }
    
                for (int i = start; i < end; i++) {
                    if (stringFilterChinese(source) && !source.toString().contains("。") && !source.toString ().contains("，")) {
                        return "";
                    } else if (CHINESE_RADICAL_DIGISTS.contains(source)) {
                        return "";
                    }
                }
                return null;
            }
        };
        return filter;
    }
    
    
    /**
     * 限制只能输入汉字，过滤非汉字
     *
     * @param str 输入值
     * @return true 非汉字；false 汉字
     */
    public boolean stringFilterChinese(CharSequence str) {
         //只允许汉字，正则表达式匹配出所有非汉字
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }
    mEdtAddDictation.setFilters(new InputFilter[]{getInputFilter()});

```
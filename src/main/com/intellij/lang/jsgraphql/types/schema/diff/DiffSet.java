/*
    The MIT License (MIT)

    Copyright (c) 2015 Andreas Marek and Contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
    (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge,
    publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
    so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
    LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
    CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.intellij.lang.jsgraphql.types.schema.diff;

import com.intellij.lang.jsgraphql.types.PublicApi;

import java.util.Map;


@PublicApi
public class DiffSet {

    private final Map<String, Object> introspectionOld;
    private final Map<String, Object> introspectionNew;

    public DiffSet(Map<String, Object> introspectionOld, Map<String, Object> introspectionNew) {
        this.introspectionOld = introspectionOld;
        this.introspectionNew = introspectionNew;
    }

    /**
     * @return the old API as an introspection result
     */
    public Map<String, Object> getOld() {
        return introspectionOld;
    }

    /**
     * @return the new API as an introspection result
     */
    public Map<String, Object> getNew() {
        return introspectionNew;
    }

}

/*
 * The MIT License
 *
 * Copyright 2014 Marko Zivanovic.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rs.in.zivanovic.share.a.secret.api.controllers;

import org.springframework.validation.BindingResult;
import rs.in.zivanovic.share.a.secret.api.dto.SasResponse;

/**
 * Base class for our controllers. It contains common methods used across different controllers.
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
public abstract class AbstractSasController {

    protected SasResponse processValidationErrors(BindingResult br) {
        SasResponse r = SasResponse.badRequest();
        br.getFieldErrors().stream().forEach(err -> {
            r.withInvalidParameterValueError(err.getField(), err.getRejectedValue(), err.getDefaultMessage());
        });
        return r;
    }

}
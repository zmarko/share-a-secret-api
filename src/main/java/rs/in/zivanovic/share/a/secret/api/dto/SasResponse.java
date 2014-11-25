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
package rs.in.zivanovic.share.a.secret.api.dto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Every call to share-a-secret API service returns object of SasResponse class
 * as response.
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
public class SasResponse {

    public static SasResponse ok() {
        return new SasResponse(HttpStatus.OK);
    }

    public static SasResponse badRequest() {
        return new SasResponse(HttpStatus.BAD_REQUEST);
    }

    public static SasResponse status(HttpStatus status) {
        return new SasResponse(status);
    }

    public SasResponse withData(Shares shares) {
        data = shares;
        return this;
    }

    public SasResponse withInvalidParameterValueError(String field, Object value, String reason) {
        return withError(Error.INVALID_PARAMETER_VALUE, String.format("Invalid value '%s' for field '%s' because '%s'", value, field, reason));
    }

    public SasResponse withError(int code, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new Error(code, message));
        status = Status.ERROR;
        return this;
    }

    private SasResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = Status.OK;
    }

    public ResponseEntity build() {
        return new ResponseEntity(this, httpStatus);
    }

    public enum Status {

        OK, ERROR;
    }

    private Status status;
    private Object data;
    private List<Error> errors;
    private final HttpStatus httpStatus;

    public Status getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static class Error {

        public static final int INVALID_PARAMETER_VALUE = 1;

        private final int code;
        private final String message;

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}

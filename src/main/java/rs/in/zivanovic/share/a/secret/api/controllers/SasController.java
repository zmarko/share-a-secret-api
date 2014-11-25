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

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import rs.in.zivanovic.share.a.secret.api.dto.Shares;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.in.zivanovic.share.a.secret.api.SecretShare;
import rs.in.zivanovic.share.a.secret.api.ShamirSecretSharing;
import rs.in.zivanovic.share.a.secret.api.dto.SasResponse;
import rs.in.zivanovic.share.a.secret.api.dto.SplitParameters;

/**
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
@RestController
@RequestMapping("/sas")
public class SasController {

    @RequestMapping(method = RequestMethod.POST, value = "/split")
    public ResponseEntity split(@Valid @RequestBody SplitParameters params, BindingResult br) {
        if (br.hasErrors()) {
            SasResponse resp = SasResponse.badRequest();
            br.getFieldErrors().stream().forEach(err -> {
                resp.withInvalidParameterValueError(err.getField(), err.getRejectedValue(), err.getDefaultMessage());
            });
            return resp.build();
        }
        if (params.getThreshold() > params.getTotal()) {
            return SasResponse.badRequest().withInvalidParameterValueError("threshold", params.getThreshold(),
                    "must not be grater than 'total' (" + String.valueOf(params.getTotal()) + ")").build();
        }
        List<SecretShare> shares = ShamirSecretSharing.split(params.getSecret(), params.getTotal(), params.
                getThreshold());
        return SasResponse.ok().withData(new Shares(shares)).build();
    }
}

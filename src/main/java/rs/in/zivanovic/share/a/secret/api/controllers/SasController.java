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

import rs.in.zivanovic.share.a.secret.api.dto.JoinParameters;
import rs.in.zivanovic.share.a.secret.api.dto.JoinResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import rs.in.zivanovic.share.a.secret.api.dto.SplitResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.in.zivanovic.share.a.secret.api.dto.SasResponse;
import rs.in.zivanovic.share.a.secret.api.dto.SplitParameters;
import rs.in.zivanovic.share.a.secret.api.dto.VersionResponse;
import rs.in.zivanovic.sss.SasUtils;
import rs.in.zivanovic.sss.SecretShare;
import rs.in.zivanovic.sss.ShamirSecretSharing;

/**
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
@RestController
@RequestMapping("/sas")
public class SasController {

    @Value("${info.build.version:'<N/A>'}")
    private String version;

    @RequestMapping(method = RequestMethod.POST, value = "/split")
    public ResponseEntity split(@Valid @RequestBody SplitParameters params, BindingResult br) {
        SasResponse response;
        if (br.hasErrors()) {
            response = processValidationErrors(br);
        } else if (params.getThreshold() > params.getTotal()) {
            response = SasResponse.badRequest().withInvalidParameterValueError("threshold", params.getThreshold(),
                    "must not be grater than 'total' (" + String.valueOf(params.getTotal()) + ")");
        } else {
            List<SecretShare> shares = ShamirSecretSharing.split(params.getSecret(), params.getTotal(),
                    params.getThreshold());
            response = SasResponse.ok().withData(new SplitResponse(shares));
        }
        return response.build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/join")
    public ResponseEntity join(@Valid @RequestBody JoinParameters params, BindingResult br) {
        SasResponse response;
        if (br.hasErrors()) {
            response = processValidationErrors(br);
        } else {
            try {
                String secret = ShamirSecretSharing.joinToUtf8String(decodeSecretShares(params));
                response = SasResponse.ok().withData(new JoinResponse(secret));
            } catch (RuntimeException ex) {
                response = SasResponse.badRequest().withInvalidParameterValueError("shares", params.getShares(),
                        "one or more shares are invalid");
            }
        }
        return response.build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/version")
    public ResponseEntity version() {
        String buildTime = "<N/A>";
        return SasResponse.ok().withData(new VersionResponse(version, buildTime)).build();
    }

    private SasResponse processValidationErrors(BindingResult br) {
        SasResponse r = SasResponse.badRequest();
        br.getFieldErrors().stream().forEach(err -> {
            r.withInvalidParameterValueError(err.getField(), err.getRejectedValue(), err.getDefaultMessage());
        });
        return r;
    }

    private List<SecretShare> decodeSecretShares(JoinParameters params) {
        List<SecretShare> shares = new ArrayList<>(params.getShares().size());
        params.getShares().stream().forEach(share -> {
            shares.add(SasUtils.decodeFromBinary(Base64.getDecoder().decode(share)));
        });
        return shares;
    }
}

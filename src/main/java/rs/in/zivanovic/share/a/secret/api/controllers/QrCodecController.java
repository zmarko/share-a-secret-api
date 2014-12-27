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

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.in.zivanovic.share.a.secret.api.dto.SasResponse;

/**
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
@RestController
@RequestMapping("/qr")
public class QrCodecController extends AbstractSasController {

    private static final List<Integer> VALID_SIZES = Arrays.asList(50, 100, 200, 250);

    @RequestMapping(method = RequestMethod.GET, value = "/encode")
    public ResponseEntity encode(
            @RequestParam(value = "text", required = true) String text,
            @RequestParam(value = "size", required = false, defaultValue = "250") int size,
            @RequestParam(value = "ecl", required = false, defaultValue = "H") ErrorCorrectionLevel ecl) {
        ResponseEntity response;
        if (!VALID_SIZES.contains(size)) {
            response = SasResponse.badRequest().
                    withError(SasResponse.Error.INVALID_PARAMETER_VALUE,
                            "Invalid QR code size, supported sizes: " + VALID_SIZES.toString()).build();
        } else {
            ByteArrayOutputStream baos = QRCode.from(text).to(ImageType.JPG).
                    withSize(size, size).withCharset(StandardCharsets.US_ASCII.name()).
                    withErrorCorrection(ecl).stream();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpeg");
            response = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
        return response;
    }

}

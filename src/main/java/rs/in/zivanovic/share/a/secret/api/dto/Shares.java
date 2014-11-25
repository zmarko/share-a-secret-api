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
import org.apache.tomcat.util.codec.binary.Base64;
import rs.in.zivanovic.share.a.secret.api.SecretShare;
import rs.in.zivanovic.share.a.secret.api.Utils;

/**
 *
 * @author Marko Zivanovic <marko@zivanovic.in.rs>
 */
public class Shares {

    private List<String> shares = new ArrayList<>();

    public Shares(List<SecretShare> shares) {
        this.shares.clear();
        shares.stream().forEachOrdered(share -> {
            this.shares.add(Base64.encodeBase64String(Utils.encodeToBinary(share)));
        });
    }

    public Shares() {
    }

    public List<String> getShares() {
        return shares;
    }

    public void setShares(List<String> shares) {
        this.shares = shares;
    }
}

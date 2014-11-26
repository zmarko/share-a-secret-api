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

angular.module('sasControllers', []).
        controller('MainController', ['$scope', 'SplitService',
            function ($scope, SplitService) {

                $scope.total = 10;
                $scope.threshold = 3;

                var clickedShares = {};

                $scope.isShareClicked = function (share) {
                    return clickedShares[share] !== undefined;
                };

                $scope.shareClicked = function (share) {
                    clickedShares[share] = true;
                };

                $scope.split = function () {
                    if ($scope.secret === undefined || $scope.secret.length === 0 ||
                            $scope.total === undefined || Number($scope.total) > 100 ||
                            $scope.threshold === undefined || Number($scope.threshold) > 99) {
                        return;
                    }
                    $scope.errors = [];
                    $scope.shares = [];
                    clickedShares = {};
                    SplitService.split({
                        secret: $scope.secret,
                        total: $scope.total,
                        threshold: $scope.threshold},
                    function (response) {
                        if (response.status === 'OK') {
                            $scope.shares = response.data.shares;
                            $scope.sharesAsText = mergeSharesToString(response.data.shares);
                        } else {
                            $scope.errors = response.errors;
                        }
                    }, function (response) {
                        $scope.errors = response.data.errors;
                    });
                };
            }]);

function mergeSharesToString(shares) {
    var res = "";
    for (var i in shares) {
        res = res + shares[i] + (i < shares.length - 1 ? "\n" : "");
    }
    return res;
}


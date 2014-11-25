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
                    console.log('iA: ' + JSON.stringify(clickedShares));
                    console.log('iO: ' + clickedShares[share]);
                    return clickedShares[share] !== undefined;
                };

                $scope.shareClicked = function (share) {
                    console.log('cA: ' + JSON.stringify(clickedShares));
                    console.log('cO: ' + clickedShares[share]);
                    clickedShares[share] = true;
                };

                $scope.split = function () {
                    $scope.errors = [];
                    $scope.shares = [];
                    clickedShares = {};
                    SplitService.split({
                        secret: $scope.secret,
                        total: $scope.total,
                        threshold: $scope.threshold},
                    function (response) {
                        console.log(JSON.stringify(response));
                        if (response.status === 'OK') {
                            $scope.shares = response.data.shares;
                        } else {
                            $scope.errors = response.errors;
                            console.log("Errors: " + JSON.stringify(response.errors));
                        }
                    }, function (response) {
                        console.log(JSON.stringify(response));
                        $scope.errors = response.data.errors;
                        console.log("Errors: " + JSON.stringify(response.data.errors));
                    });
                };
            }]);
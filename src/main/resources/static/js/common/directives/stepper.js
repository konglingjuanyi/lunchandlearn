/**
 * This is an angular wrapper for a jquery numeric stepper
 * Found i nthe lib directory and at http://xflatlinex.github.io/Numeric-Stepper/
 */
angular.module('directives').directive('stepper', function () {
	'use strict';
	return {
		restrict: 'EA',
		require: 'ngModel',
		scope: {
			options: '=?'
		},
		link: function (scope, element, attrs, ngModel) {
			var _operations = {add: 'add', multiply: 'multiply'};
			var defaultOptions = {
				limit : [0, 2147483647], // default limit is to not include negative values
				wheelStep : 1,
				arrowStep : 1,
				operation: 'add',
				type:'int',
				incrementButton : '<i class="nc-icon-mini arrows-1_small-triangle-up"></i>',
				decrementButton : '<i class="nc-icon-mini arrows-1_small-triangle-down"></i>',
				floatPrecission: 2// decimal precission
			};
			var options = angular.extend(defaultOptions, scope.options);
			var previousValue = 0;
			options.onStep =  function( val, up ){
				//Fired each time the value is changed by the buttons, arrow keys or mouse wheel.
				if(this.context.disabled){
					this.context.value = previousValue;
				} else {
					scope.$parent.$apply(function () {
						if(options.operationType === _operations.multiply) {
							var min = options.limit[0], max = options.limit[1];
							var product = min;
							if(product === 0) {
								product = 1;
							}
							var previousProduct = product;
							while(product >= min && product <= max) {
								if(up && product >= val) {
									val = product;
									break;
								}
								else if(!up && product === val) {
									val = product;
									break;
								}
								else if(!up && product > val) {
									val = previousProduct;
									break;
								}
								previousProduct = product;
								product *= options.arrowStep;
							}
							if(product > max) {
								val = product / options.arrowStep;
							}

							$('#' + scope.options.inputId).val(val);
						}
						ngModel.$setViewValue(val);
					});
				}
			};

			scope.$watch(function(){return ngModel.$viewValue;}, function(newValue){
				var val;
				/*let the user enter value explicitly,(currently the case of tile width)
				 * Example: If tile has already a width value as 16, and
				 *  user(manually not through stepper) remove 6 to make it e.g:17,
				 *   value becomes 16 again as 1 is less than 16 
				*/
				if(options.enableMinimum) {
					val = newValue;
				}
				else {
					if(newValue > options.limit[1] || newValue < options.limit[0]){
						val = previousValue;
					} else {
					val = newValue;
					}
				}
				if(val !== newValue){
					ngModel.$setViewValue(parseInt(val));
					ngModel.$render();
				}
				previousValue = val;
			});
			element.stepper(options);
		}
	};
});
angular.module('lunchAndLearnApp').directive('required', function() {
	return function(scope, element) {
		element.parents(".form-group").children(":nth-child(2)").append("<span style='color: red'><strong>&nbsp;*</strong></span>");
	};
});
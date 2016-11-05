angular.module('pbAcademyApp').directive('required', function() {
	return function(scope, element) {
		element.append(".form-group").children(":nth-child(2)").append("<span style='color: red'><strong>&nbsp;*</strong></span>");
	};
});
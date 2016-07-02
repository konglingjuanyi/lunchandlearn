/**
 * Created by de007ra on 5/7/2016.
 */
angular.module('directives').directive('topicListMini', function() {
	return {
		templateUrl : '/lunchandlearn/html/topic/topicListMini.html',
		replace : true,
		scope: {
			topics: '=?',
			topicHeading: '@'
		}
	};
});
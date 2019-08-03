<?php 

	if (isset($_POST['Username']) && isset($_POST['Password']) && isset($_POST['Url'])) {
		$Account= $_POST['Username'];
		$Key= $_POST['Password'];
		$Url=$_POST['Url'];
	}


$Url= '/Reports/Form/StudentTimeTable.aspx';

	login("http://qldt.actvn.edu.vn/CMCSoft.IU.Web.Info/Login.aspx", "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUKMTkwNDg4MTQ5MQ9kFgICAQ9kFgpmD2QWCgIBDw8WAh4EVGV4dAUjSOG7jEMgVknhu4ZOIEvhu7ggVEhV4bqsVCBN4bqsVCBNw4NkZAICD2QWAmYPDxYEHwAFDcSQxINuZyBuaOG6rXAeEENhdXNlc1ZhbGlkYXRpb25oZGQCAw8QDxYGHg1EYXRhVGV4dEZpZWxkBQZreWhpZXUeDkRhdGFWYWx1ZUZpZWxkBQJJRB4LXyFEYXRhQm91bmRnZBAVAgJWTgJFThUCIEU0MzI5NkM2RjI0QzQ0MTBBODk0RjQ2RDU3RDJEM0FCIEFCQUY0NkJENjcxMjQ3QzVCNUI1Mjg4NUJCMkY5QzQ5FCsDAmdnFgFmZAIEDw8WAh4ISW1hZ2VVcmwFKC9DTUNTb2Z0LklVLldlYi5JbmZvL0ltYWdlcy9Vc2VySW5mby5naWZkZAIFD2QWBgIBDw8WAh8ABQZLaMOhY2hkZAIDDw8WAh8AZWRkAgcPDxYCHgdWaXNpYmxlaGRkAgIPZBYEAgMPD2QWAh4Gb25ibHVyBQptZDUodGhpcyk7ZAIHDw8WAh8AZWRkAgQPDxYCHwZoZGQCBg8PFgIfBmhkFgYCAQ8PZBYCHwcFCm1kNSh0aGlzKTtkAgUPD2QWAh8HBQptZDUodGhpcyk7ZAIJDw9kFgIfBwUKbWQ1KHRoaXMpO2QCCw9kFghmDw8WAh8AZWRkAgEPZBYCZg8PFgIfAWhkZAICD2QWAmYPDxYEHwAFDcSQxINuZyBuaOG6rXAfAWhkZAIDDw8WAh8ABbcFPGEgaHJlZj0iIyIgb25jbGljaz0iamF2YXNjcmlwdDp3aW5kb3cucHJpbnQoKSI%2BPGRpdiBzdHlsZT0iRkxPQVQ6bGVmdCI%2BCTxpbWcgc3JjPSIvQ01DU29mdC5JVS5XZWIuSW5mby9pbWFnZXMvcHJpbnQucG5nIiBib3JkZXI9IjAiPjwvZGl2PjxkaXYgc3R5bGU9IkZMT0FUOmxlZnQ7UEFERElORy1UT1A6NnB4Ij5JbiB0cmFuZyBuw6B5PC9kaXY%2BPC9hPjxhIGhyZWY9Im1haWx0bzo%2Fc3ViamVjdD1IZSB0aG9uZyB0aG9uZyB0aW4gSVUmYW1wO2JvZHk9aHR0cDovL3FsZHQuYWN0dm4uZWR1LnZuL0NNQ1NvZnQuSVUuV2ViLkluZm8vTG9naW4uYXNweCI%2BPGRpdiBzdHlsZT0iRkxPQVQ6bGVmdCI%2BPGltZyBzcmM9Ii9DTUNTb2Z0LklVLldlYi5JbmZvL2ltYWdlcy9zZW5kZW1haWwucG5nIiAgYm9yZGVyPSIwIj48L2Rpdj48ZGl2IHN0eWxlPSJGTE9BVDpsZWZ0O1BBRERJTkctVE9QOjZweCI%2BR%2BG7rWkgZW1haWwgdHJhbmcgbsOgeTwvZGl2PjwvYT48YSBocmVmPSIjIiBvbmNsaWNrPSJqYXZhc2NyaXB0OmFkZGZhdigpIj48ZGl2IHN0eWxlPSJGTE9BVDpsZWZ0Ij48aW1nIHNyYz0iL0NNQ1NvZnQuSVUuV2ViLkluZm8vaW1hZ2VzL2FkZHRvZmF2b3JpdGVzLnBuZyIgIGJvcmRlcj0iMCI%2BPC9kaXY%2BPGRpdiBzdHlsZT0iRkxPQVQ6bGVmdDtQQURESU5HLVRPUDo2cHgiPlRow6ptIHbDoG8gxrBhIHRow61jaDwvZGl2PjwvYT5kZGTK3ehZSKLiDVebc6SGrLAIRFRdqEqfzLdFxjJKSLA3LQ%3D%3D&__VIEWSTATEGENERATOR=D620498B&__EVENTVALIDATION=%2FwEdABABoCZ6vXASqQ3a11z3tS9ob8csnTIorMPSfpUKU79Fa8zr1tijm%2FdVbgMI0MJ%2F5MjbtizDfvhUgF5WgsmKWRe6NC9vVRzw%2Fqnrl8xh27lNCR1v4rFMsOsPLazQBhw3uU%2F8NpSLPRO3nldgsLGSmpvMhvbRLf0w8iWtX8wHSHJ0bmN6ZYJNGAQHn0c9zMgZU3B2NvjHOkq5wKoqN6Aim8WGPOaW1pQztoQA36D1w%2F%2BbXQMU5uF05x9VwXgdUQCd6MJcd7JJKNJpqcDZrL6E%2FrOydMv9ZkwzqTYChEKPqLTBMzEttfn4WHW1%2Bx4xjkWsEDKqXTaSpLcLZBVGEu9Q8xr%2BSqRI5fNbf9CdZ3pE5cUKhVnq4monB4QzhLSXAbx7GrQ%3D&PageHeader1%24drpNgonNgu=E43296C6F24C4410A894F46D57D2D3AB&PageHeader1%24hidisNotify=0&PageHeader1%24hidValueNotify=.&txtUserName=".$Account."&txtPassword=".$Key."&btnSubmit=%C4%90%C4%83ng+nh%E1%BA%ADp&hidUserId=&hidUserFullName=&hidTrainingSystemId=");
	echo grab_page("http://qldt.actvn.edu.vn/CMCSoft.IU.Web.Info".$Url);

	
	
	function login($url, $data){
		 
		$login= curl_init();
		curl_setopt($login, CURLOPT_COOKIEJAR, "/tmp/cookie");
		curl_setopt($login, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($login, CURLOPT_TIMEOUT,40000);   
		curl_setopt($login, CURLOPT_URL, $url);  // url đăng nhập
		curl_setopt($login, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']);   // Giả lập trình duyệt
		curl_setopt($login, CURLOPT_FOLLOWLOCATION, true ); 
		curl_setopt($login, CURLOPT_POST, true);
		curl_setopt($login, CURLOPT_POSTFIELDS, $data);   // Điền dl dạng raw vào post
		curl_setopt($login, CURLOPT_SSL_VERIFYHOST, false);
		curl_setopt($login, CURLOPT_SSL_VERIFYPEER, false);
		ob_start();
		return curl_exec($login);
		ob_end_clean();
		curl_close($login);
		unset($login);
	}

	function grab_page($site){
		$ch= curl_init();
		curl_setopt($ch, CURLOPT_COOKIEFILE, "/tmp/cookie"); // Lưu cookie
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_TIMEOUT,40000);
		curl_setopt($ch, CURLOPT_URL, $site);  // url 
		curl_setopt($ch, CURLOPT_USERAGENT, $_SERVER['HTTP_USER_AGENT']);   // Giả lập trình duyệt
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true ); 
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		ob_start();

		if (curl_error($ch)) {
    		 echo curl_error($ch);
		}
		return curl_exec($ch);
		
		ob_end_clean();
		curl_close($ch);
	}

?>






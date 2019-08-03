<?php 
	if (isset($_POST['Username']) && isset($_POST['Password'])){
		$Username= $_POST['Username'];
		$Password= $_POST['Password'];
	}
  

	
	$Url= '/Reports/Form/StudentTimeTable.aspx';
	
	class TKB{
		public $LopHocPhan, $HocPhan, $ThoiGian, $DiaDiem, $GiangVien, $SySo, $SoDK, $SoTC;

		function TKB($lophocphan, $hocphan, $thoigian, $diadiem, $giangvien, $syso, $sodk, $sotc){
			$this->LopHocPhan= $lophocphan;
			$this->HocPhan= $hocphan;
			$this->ThoiGian= $thoigian;
			$this->DiaDiem= $diadiem;
			$this->GiangVien= $giangvien;
			$this->SySo= $syso;
			$this->SoDK= $sodk;
			$this->SoTC= $sotc;
			
		}
	}

	include('simple_html_dom.php');
    // Gửi dữ liệu POST vào login.php
	$request = array(
					'http' => array(
				    'method' => 'POST',
				    'content' => http_build_query(array(
				        'Username' => $Username,
				        'Password' => $Password,
				        'Url' => $Url
				    ))
					),
					);

	$context = stream_context_create($request);
	$url = 'http://kmatkb.freevar.com/TKB/login.php';
    $html = file_get_html($url, false, $context);
    //echo $html;
    if($html->find('p[class="style1"]')){
    	echo "Login missed";
    }else
    {
	    
	    $ListThoiGian= array();
	    $ListTKB= array();



	    $tableMonHocs=$html->find("//*[@id='gridRegistered']");
	    //echo count($tableMonHocs);
	    foreach ($tableMonHocs as $tableMonHoc) {
	    	
		    $Node_trs= $tableMonHoc->find("tr");
		    //echo $Node_trs[0];
		    foreach ($Node_trs as $Node_tr) {
		    	# Lớp học phần...
		    	$LopHocPhan= $Node_tr->find("td", 1);
		    	$LopHocPhan= strip_tags($LopHocPhan);
		    	$LopHocPhan= preg_replace('/\s\s+/', ' ', trim($LopHocPhan));
		    	//echo $LopHocPhan." - ";
		    	

		    	$HocPhan= $Node_tr->find("td", 2);
		    	$HocPhan= strip_tags($HocPhan);
		    	$HocPhan= preg_replace('/\s\s+/', ' ', trim($HocPhan));
		    	//echo $HocPhan."</br>";

		    	/**
					Cần tách thời gian ra thành các thuộc tính:
					+ Mã thời gian (Object)
					+ Ngày bắt đầu
					+ Ngày kết thúc
					+ Thứ 
					+ Tiết
		    	**/
		    	$ThoiGian= $Node_tr->find("td", 3);
		    	$ThoiGian= strip_tags($ThoiGian);
		    	$ThoiGian= preg_replace('/\s\s+/', ' ', trim($ThoiGian));
		    	//echo $ThoiGian."</br>";
		    	$ThoiGian= TachThoiGian($ThoiGian);  // Hàm tách

		    	/**
					Địa điểm cũng tùy thuộc vào thời gian
					Nếu địa điểm gồm nhiều nơi khác nhau thì:
					Tách ra thành mảng gồm:
					+ Mã thời gian
					+ Địa điểm
		    	**/
		    	$DiaDiem= $Node_tr->find("td", 4);
		    	$DiaDiem= strip_tags($DiaDiem);
		    	$DiaDiem= preg_replace('/\s\s+/', ' ', trim($DiaDiem));
		    	// Tồn tại nhiều địa điểm ? (Mã thời gian đứng đầu)
		    	if (strpos($DiaDiem, '(')!==false && strpos($DiaDiem, '(')==0 ){
		    		$DiaDiem= TachDiaDiem($DiaDiem);
		    	}else{
		    		$d= array();
		    		$m= array();
		    		$n= array();
		    		array_push($n, 0);

		    		$m['MaThoiGians']= $n; 
		    		$m['ChoHoc']= $DiaDiem; 
		    		array_push($d, $m); 
		    		$DiaDiem= $d;
		    	}
		    	//echo json_encode($DiaDiem)."</br>";
		    	

		    	$GiangVien= $Node_tr->find("td", 5);
		    	$GiangVien= strip_tags($GiangVien);
		    	$GiangVien= preg_replace('/\s\s+/', ' ', trim($GiangVien));
		    	//echo $GiangVien."</br>";

		    	$SySo= $Node_tr->find("td", 6);
		    	$SySo= strip_tags($SySo);
		    	$SySo= preg_replace('/\s\s+/', ' ', trim($SySo));
		    	//echo $SySo."</br>";

		    	$SoDK= $Node_tr->find("td", 7);
		    	$SoDK= strip_tags($SoDK);
		    	$SoDK= preg_replace('/\s\s+/', ' ', trim($SoDK));
		    	//echo $SoDK."</br>";

		    	$SoTC= $Node_tr->find("td", 8);
		    	$SoTC= strip_tags($SoTC);
		    	$SoTC= preg_replace('/\s\s+/', ' ', trim($SoTC));
		    	//echo $SoDK."</br>";

		    	$MH= new TKB($LopHocPhan, $HocPhan, $ThoiGian, $DiaDiem, $GiangVien, $SySo, $SoDK, $SoTC);
		    	array_push($ListTKB, $MH);
		    }
		}
		
		echo '{"TKB":';
		echo json_encode($ListTKB);
		echo '}';
	}

	function TachThoiGian($TG){
		if (strpos($TG, ': (')){
			$ListTG= array(); 
			$ListTG["ThoiGian"]= array(); 
			
			
			while(strpos($TG, 'TH') || strpos($TG, 'LT')){  // Xét đk chuỗi tồn tại chuỗi tiết nữa hay ko?
				//echo $TG."---- Bắt đàu tách ngày và mã </br>";
				$a = array(); // Mảng lưu ngày bđ kt và mã  
				if (strpos($TG, ': (')){
					$viTri1= strpos($TG, ': (');
					$chuoi1= substr($TG, 0, $viTri1+5);   // Chuỗi chứa ngày bđ ngày kt  và mã tg
					$TG= substr($TG, $viTri1+5);   // Xóa bỏ chuỗi 1 ra khỏi chuỗi TG
					$TG= trim($TG);   // Xóa bỏ chuỗi 1 ra khỏi chuỗi TG
					
					//echo $chuoi1."--- kq ngày</br>";
					$words1= explode(' ', $chuoi1);  // Tách từ trong chuỗi ngày bđ và ngày kt và mã tg
					// Phần tử ngày bđ [1] ngày kt[3] mã [4] 
					// Xóa dấu () trong chuỗi mã 
					$words1[4]= ltrim($words1[4], '(');
					$words1[4]= rtrim($words1[4], ')');
					
					$ngayBatDau= $words1[1];
					$ngayKetThuc= $words1[3];
					$ngayKetThuc= trim($ngayKetThuc, ":");
					$maThoiGian= $words1[4];

					//echo "Ngày bắt đầu: ".$ngayBatDau .' - '. "Ngày kết thúc: ".$ngayKetThuc .' - '."Mã thời gian: ".$maThoiGian .'</br>';
					$a["MaThoiGian"]= $maThoiGian;
					$a["NgayBatDau"]= $ngayBatDau;
					$a["NgayKetThuc"]= $ngayKetThuc;
					//array_push($ListTG["ThoiGian"], $a);  // Lưu mảng chứa ngày vào mảng chính
					
				}
				
				//$Lich= array();
				//$Lich["Lich"]= array();

				//echo $TG."--- Bắt đầu tách tiết</br>";
				$ngay= array();  // Chứa mã thứ tiết
				while(1){
					if((strpos( $TG, '&n')==0 || strpos( $TG, ')&n')==0) && (strpos( $TG, '(')!=0)){// Nếu là chuỗi thứ đứng đầu
							//$t = array();
							$viTri2a= strpos($TG, ';T');   // Vị trí dấu ;) bắt đầu 1 chuỗi thứ
							$viTri2b= strpos($TG, '(');    // Vị trí kết thúc 1 chuỗi thứ
							$chuoiThu= substr($TG,$viTri2a+1, $viTri2b-14);  // Chuỗi chứa thứ và tiết
												

							//echo $chuoiThu."--- đk1</br>";
							$words2= explode(' ', $chuoiThu);
							$thu= $words2[1];
							$tiet= $words2[3];


							$TG= substr($TG, $viTri2b+3);   // Xóa bỏ chuỗi ra khỏi chuỗi TG
							
							// echo $TG." --- Két thúc tách tiét</br>";
							$t["Thu"]= $thu;
							$t["Tiet"]= $tiet;

							//array_push($Lich, $t);  // Them dữ liệu thứ và tiết vào mảng lịch
							array_push($ngay, $t);  // Them dữ liệu thứ và tiết vào mảng lịch
						
							
					}else{
							break;
					}
				}
				$a["Lich"]= array();
				array_push($a["Lich"], $ngay);  // Them dữ liệu thứ và tiết vào mảng lịch
				array_push($ListTG["ThoiGian"], $a);  // Lưu mảng chứa ngày vào mảng chính
					
					
				
			}
		}else if (strpos($TG, ':&')){
			$ListTG= array(); 
			$ListTG["ThoiGian"]= array(); 
			
			$viTri1= strpos($TG, ':&');

			$chuoi1= substr($TG, 0, $viTri1);   // Chuỗi chứa ngày bđ ngày kt  và mã tg
			$words1= explode(' ', $chuoi1);  // Tách từ trong chuỗi ngày bđ và ngày kt và mã tg
					// Phần tử ngày bđ [1] ngày kt[3]
			$a= array();
					$ngayBatDau= $words1[1];
					$ngayKetThuc= $words1[3];
					$ngayKetThuc= trim($ngayKetThuc, ":");
					
					//echo "Ngày bắt đầu: ".$ngayBatDau .' - '. "Ngày kết thúc: ".$ngayKetThuc .' - '."Mã thời gian: ".$maThoiGian .'</br>';
					$a["MaThoiGian"]= 0;
					$a["NgayBatDau"]= $ngayBatDau;
					$a["NgayKetThuc"]= $ngayKetThuc;
				
			//echo $chuoi1.'</br>';

			$viTri2= strpos($TG, ';T');
			$chuoi2= substr($TG, $viTri2);   // Chuỗi chứa tiết
			//echo $chuoi2.'</br>'.'</br>';
			//echo strpos( $chuoi2, ';T');

			$ngay= array();  // Chứa mã thứ tiết
			while(1){
				if((strpos( $chuoi2, ';T')==0 && (strpos( $chuoi2, ';T')!==false)) || (strpos( $chuoi2, ')&n')==0 && (strpos($chuoi2, ')&n')!==false))){
						$viTri2a= strpos($chuoi2, ';T');   // Vị trí dấu ;) bắt đầu 1 chuỗi thứ
						$viTri2b= strpos($chuoi2, '(');    // Vị trí kết thúc 1 chuỗi thứ
						$chuoiThu= substr($chuoi2,$viTri2a+1, $viTri2b);  // Chuỗi chứa thứ và tiết
															

						//echo $chuoiThu."--- đk1</br>";
						$words2= explode(' ', $chuoiThu);
						$words2[1]= ltrim($words2[1], '(');
						$thu= $words2[1];
						$tiet= $words2[3];
						//echo $chuoiThu.'</br>';
						// echo $thu.'</br>';
						// echo $tiet.'</br>';
						


						$chuoi2= substr($chuoi2, $viTri2b+3);   // Xóa bỏ chuỗi ra khỏi chuỗi TG
										
										// echo $TG." --- Két thúc tách tiét</br>";
						$t["Thu"]= $thu;
						$t["Tiet"]= $tiet;

						//array_push($Lich, $t);  // Them dữ liệu thứ và tiết vào mảng lịch
						array_push($ngay, $t);  // Them dữ liệu thứ và tiết vào mảng lịch

					}else
						break;
			}
						
			$a["Lich"]= array();
			array_push($a["Lich"], $ngay);  // Them dữ liệu thứ và tiết vào mảng lịch
			array_push($ListTG["ThoiGian"], $a);  // Lưu mảng chứa ngày vào mảng chính
					
		}
		// echo json_encode($ListTG;);
		// echo "</br>";
		return $ListTG;
	}

	function TachDiaDiem($diadiem ){
		$d= array();
		$m= array();
		while(strpos($diadiem, '(')!==false && strpos($diadiem, '(')==0 && strpos($diadiem, '(T')!==0){
			if (strlen($diadiem)>16 && strpos($diadiem, '(', 16)){
				$viTri= strpos($diadiem, '(', 16);  // Nếu tồn tại ngoặc ( của mã thời gian (Không phải của địa điểm)
				$chuoi1= substr($diadiem, 0, $viTri);  /// Chứa mã thời igan và địa điểm
				$diadiem= substr($diadiem,$viTri);
			}else {
				$chuoi1= substr($diadiem, 0);  /// Chứa mã thời igan và địa điểm
				$diadiem= substr($diadiem, strlen($diadiem));
			}
			// Tách chuoi1 thành cma va cdiadiem
			$words= explode(')', $chuoi1);
			// cma o vi tri 0 , cdiadiem v tri 1

			//$m['MaThoiGians']= array();
			$cma= $words[0];
			$cma= ltrim($cma, '(');
			if (strpos($cma, ',')){
				$words1= explode(',', $cma);   // Chứa phần tử mã
				// Vòng lặp để lấy các mã thời gian...
				 
				$m['MaThoiGians']= $words1;  // Them dữ liệu thứ và tiết vào mảng lịch
				
			}else{
				// Nếu chỉ có một mã
				$ma= array();
				array_push($ma, $cma);
				$m['MaThoiGians']= $ma;
			}
			

			$cdiadiem= $words[1];
			$cdiadiem= ltrim($cdiadiem, ') ');

			$m['ChoHoc']= $cdiadiem;  
			array_push($d, $m);  // 
							

		// 	echo $chuoi1."--- Dia diem</br>";
		// 	echo $diadiem."--- KQ Dia diem</br></br>";
		 }
		//echo json_encode($d);  
		return $d;

	}
	
?>
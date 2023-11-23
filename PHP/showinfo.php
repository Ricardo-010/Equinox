<?php

//PHP to retrieve information pertaining to a user - 'problems' - for the 'i' icon in chats.

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$patient_username = $_POST["patient_username"];
$counsellor_username = $_POST["counsellor_username"];
$ID = $_POST["id"];

$output = array();
$output1 = array();

//Patient Information obtained
if($ID == 'C'){
	if($result = mysqli_query($link, "SELECT Problem FROM Patient WHERE Patient_Username = '$patient_username'")){
   		while ($row = $result->fetch_assoc()){
       		$output[] = $row;
	}
	}
echo json_encode($output);
}


//Counsellor Information obtained

if($ID == 'P'){
	if($result = mysqli_query($link, "SELECT Problem FROM Counsellor WHERE Counsellor_Username = '$counsellor_username'")){
   		while ($row1 = $result->fetch_assoc()){
       		$output1[] = $row1;
	}
	}
echo json_encode($output1);
}



mysqli_close($link);
?>
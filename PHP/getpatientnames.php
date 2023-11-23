<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$username1 = $_POST["counsellor_username"];

$Patient_Username = array();

if ($t = mysqli_query($link, "Select Patient_Username from Patient Where Counsellor_Username = '$username1'")){	
	while ($row = $t->fetch_assoc()){
		$Patient_Username[] = $row;
	}

}

echo json_encode($Patient_Username);


mysqli_close($link);
?>
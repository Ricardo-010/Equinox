<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$username1 = $_POST["username"];
$password1 = $_POST["password"];

$output = array();

if (mysqli_fetch_assoc(mysqli_query($link, "Select * from Verify Where Username = '$username1' and Password = '$password1'"))==false){
	$output['message'] = "Login failed";
}
else{
	$output['message'] = "Successfully logged in!";
	$result = mysqli_query($link, "Select Counsellor from Verify where Username = '$username1' and Password = '$password1'");
	$result1 = mysqli_fetch_assoc($result);
	$output['yn'] = $result1;
}

echo json_encode($output);

mysqli_close($link);
?>



<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

//output for patient username
$patient_username = $_POST["patient_username"];
$counsellor_username = $_POST["counsellor_username"];
$no_patients = $_POST["no_patients"];

if (mysqli_query($link, "UPDATE Counsellor SET No_patients = '$no_patients' WHERE Counsellor_Username = '$counsellor_username'")) {

}
if (mysqli_query($link, "UPDATE Patient SET Counsellor_Username = '$counsellor_username' WHERE Patient_Username = '$patient_username'")){

}

mysqli_close($link);
?>
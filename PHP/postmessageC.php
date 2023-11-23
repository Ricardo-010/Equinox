<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$P_username= $_POST['patient_username'];
$message = $_POST['message'];
$C_username = $_POST['counsellor_username'];

if(mysqli_query($link, "INSERT INTO Messages (Patient_Username, Counsellor_Username, Person, Message) VALUES ('$P_username', '$C_username', 'C', '$message' )")){
       
}

mysqli_close($link);
?>



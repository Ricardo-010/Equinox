<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$username1 = $_POST["username"];
$output = array();

if ($r1 = mysqli_query($link, "SELECT Problem FROM Patient WHERE Patient_Username = '$username1'")) {
    while ($row1=$r1->fetch_assoc()){
        $output[]=$row1;
    }
}

echo json_encode($output);

mysqli_close($link);
?>

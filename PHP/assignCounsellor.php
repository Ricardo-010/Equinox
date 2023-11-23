<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

//ouput for getting counsellor problems
//output 1 for getting patient problems
$username1 = $_POST["username"];
$output = array();

if ($r = mysqli_query($link, "SELECT * FROM Counsellor")) {
    while ($row=$r->fetch_assoc()){
        $output[]=$row;
    }
}

echo json_encode($output);

mysqli_close($link);
?>

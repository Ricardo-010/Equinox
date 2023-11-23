<?php

// this is from the Counsellors perspective,
// it will be very similar from the Patients perspective, 
// but somethings will change

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$counsellor_username = $_POST["counsellor_username"];
$patient_username = $_POST['patient_username'];
$output = array();


    //get previous messages
if(mysqli_fetch_assoc(mysqli_query($link, "SELECT * FROM Messages WHERE Counsellor_Username = '$counsellor_username' AND Patient_Username = '$patient_username'")) == true){
  if($result = mysqli_query($link, "SELECT * FROM Messages WHERE Counsellor_Username = '$counsellor_username'  AND Patient_Username = '$patient_username'")){
      while ($row = $result->fetch_assoc()){
          $output[] = $row;
      }
  echo json_encode($output);
  }
}



mysqli_close($link);
?>
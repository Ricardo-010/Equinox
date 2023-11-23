<?php

// this is from the Patient's perspective,
// it will be very similar from the counsellors perspective, 
// but somethings will change

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$patient_username = $_POST["patient_username"];
$Login = $_POST["login"];

$output = array();

if ($Login == "true"){
    //get previous messages
  if(mysqli_fetch_assoc(mysqli_query($link, "SELECT * FROM Messages WHERE Patient_Username = '$patient_username'")) == true){
    if($result = mysqli_query($link, "SELECT * FROM Messages WHERE Patient_Username = '$patient_username'")){
        while ($row = $result->fetch_assoc()){
            $output[] = $row;
        }
    echo json_encode($output);
    }
  }

    else{
	$Counselor_output = array();
    	$r = mysqli_query($link, "SELECT Counsellor_Username FROM Patient WHERE Patient_Username = '$patient_username'");
        $Counselor_name[] = $r->fetch_assoc();
	echo json_encode($Counselor_name);
    }
    

    //inserting messages
    //mysqli_query($link, "INSERT INTO Chats_list (Patient_Username, Counsellor_Username) VALUES ('$P_username', '$Counselor_name')");
}
else{
    $Counselor_output = array();

    //get the Counsellors name who the patient is talking with
    if($r = mysqli_query($link, "SELECT Counsellor_Username FROM Patient WHERE Patient_Username = '$patient_username'")){
        $Counselor_name[] = $r->fetch_assoc();
    }
    //Outputs the counsellors name
    echo json_encode($Counselor_name);
    
    //inserting messages
}

mysqli_close($link);
?>
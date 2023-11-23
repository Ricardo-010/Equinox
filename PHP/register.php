
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
$yn = $_POST["yn"];

// Perform query

if (mysqli_fetch_assoc(mysqli_query($link, "SELECT * FROM Verify WHERE Username = '$username1'")) == false){
	$output['message'] = "You have successfully registered!";
    // Perform query
    if (mysqli_query($link, "INSERT INTO Verify (Username, Password, Counsellor) VALUES ('$username1', '$password1', '$yn')")) {
     //Checks if its a patient or a counsellor and inserts them into the corresponding tables
        if($yn == "YES"){
            if (mysqli_query($link, "INSERT INTO Counsellor (Counsellor_Username) VALUES ('$username1')")){

            }
        }
    else{
        if (mysqli_query($link, "INSERT INTO Patient (Patient_Username) VALUES ('$username1')")){

        }
    }    
    }
}
else{
	$output['message'] = "This username is already in use";
}

echo json_encode($output);

mysqli_close($link);
?>

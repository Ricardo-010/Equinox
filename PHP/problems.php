<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$username = "s2427724";
$password = "s2427724";
$database = "d2427724";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$username1 = $_POST['username'];
$problems = $_POST['problems'];

$yn = $_POST['yn'];

// Perform Query

if ($yn == "YES"){
    if(mysqli_query($link, "UPDATE Counsellor SET Problem = '$problems' WHERE Counsellor_Username = '$username1'")){

    }
}
else{
    if(mysqli_query($link, "UPDATE Patient SET Problem = '$problems' WHERE Patient_Username = '$username1'")){

    }
}

mysqli_close($link);
?>

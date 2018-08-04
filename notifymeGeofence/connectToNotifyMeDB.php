<?php

$dbservername="localhost";
$dbusername="root";
$dbpassword="rahul";
$dbname="notify_me";


$connection = new mysqli($dbservername, $dbusername, $dbpassword, $dbname);
if(!$connection)
{
	die("I am Dead Connection".$connection->connect_error);
} 
if($connection){
	echo "Connected successfully to db: ".$dbname."";
}

?>
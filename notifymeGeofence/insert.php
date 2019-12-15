<?php
include "connect.php";

$enrol=$_POST["enno"];
$sname=$_POST["studname"];
$semester=$_POST["sem"];

$qr="insert into student_detail (EnNo,StudentName,Sem) values ('$enrol','$sname','$semester')";

$result=mysqli_query($con,$qr);

$data=array();

if($result=="1")
{
	$data["yes"]="1";
}
else
{
	$data["yes"]="0";
}

echo json_encode($data);


?>
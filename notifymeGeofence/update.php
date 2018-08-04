<?php
include "connect.php";

$enrol=$_POST["edittextEnno"];
$sname=$_POST["edittextstdname"];
$semester=$_POST["edittextseme"];

$qr="update student_detail set StudentName='$sname',Sem='$semester' where EnNo='$enrol';";


$result=mysqli_query($con,$qr);

$data=array();

if(mysqli_num_rows($result)>=1)
{
	$data["yes"]="1";
}
else
{
	$data["yes"]="0";
}

echo json_encode($data);


?>
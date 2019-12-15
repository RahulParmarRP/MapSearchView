<?php
include "connectToNotifyMeDB.php";
//include "db_helper.php";



	$productItem=$_REQUEST['productItem'];
	$userLat=$_REQUEST['currentLat'];
	$userLon=$_REQUEST['currentLon'];
	$userRadiusInKm= $_REQUEST['userRadiusInKm'];

	
	/*
	$productItem="mobile";
	$userLat=22.28504327;
	$userLon=70.803855545;
	$userRadiusInKm= 1.0;
*/

	



$query = "select vendorShopLat,vendorShopLon,(((acos(
sin(('$userLat'*pi()/180)) * sin((`vendorShopLat`*pi()/180))
+ cos(('$userLat'*pi()/180)) * cos((`vendorShopLat`*pi()/180))
* cos((('$userLon'- `vendorShopLon`)*pi()/180))))*180/pi())*60*1.1515*1.609344) 
AS distance 
from category_mobile where vendorProductCategory='$productItem' having distance <= '$userRadiusInKm';";




$result=mysqli_query($connection,$query);

$Array4Json=array();
$eachRowResult=array();

if(mysqli_num_rows($result)>=1)
{
$Array4Json["fetchedData"]=array();
        while( $eachRowResult = mysqli_fetch_array($result)){
			
			$neededLatLon = array();
			$neededLatLon["ShopLat"]= $eachRowResult["vendorShopLat"];
			$neededLatLon["ShopLon"]= $eachRowResult["vendorShopLon"];
			$neededLatLon["Distance"]= $eachRowResult["distance"];
			
			//$Array4Json[] = array('fetchedData' => $eachRowResult);
			array_push($Array4Json["fetchedData"],$neededLatLon);
			
		}
		
		echo json_encode($Array4Json);
}

?>
package com.crystal.bazarposmobile.dataphono;

interface CommonActivityInterface
{
	void onBarCodeReceived(String barCodeValue, int symbology);
	void onBarCodeClosed();
	void onStateChanged(String state);
}

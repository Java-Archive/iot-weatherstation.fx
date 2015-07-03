package org.rapidpm.iot.tinkerforge.weatherstation;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

import java.io.IOException;

/**
 * Created by Sven Ruppert on 17.08.2014.
 */
public class BrickletInfos {
  public static void main(String[] args) throws AlreadyConnectedException, IOException, NotConnectedException, InterruptedException {

    IPConnection ipcon = new IPConnection();
    ipcon.connect("192.168.0.200", 4223);

    ipcon.addEnumerateListener((uid, connectedUid, position, hardwareVersion, firmwareVersion, deviceIdentifier, enumerationType) -> {

          System.out.println("UID:               " + uid);
          System.out.println("Enumeration Type:  " + enumerationType);

          if (enumerationType == IPConnection.ENUMERATION_TYPE_DISCONNECTED) {
            System.out.println("");
            return;
          }


          System.out.println("Connected UID:     " + connectedUid);
          System.out.println("Position:          " + position);
          System.out.println("Hardware Version:  " + hardwareVersion[0] + "." +
              hardwareVersion[1] + "." +
              hardwareVersion[2]);
          System.out.println("Firmware Version:  " + firmwareVersion[0] + "." +
              firmwareVersion[1] + "." +
              firmwareVersion[2]);
          System.out.println("Device Identifier: " + deviceIdentifier);
          System.out.println("");
        }
    );

    ipcon.enumerate();
    Thread.sleep(2000l);
    ipcon.disconnect();
  }
}

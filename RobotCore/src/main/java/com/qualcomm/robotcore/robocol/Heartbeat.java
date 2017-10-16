/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Heartbeat message
 * <p>
 * Used to know if the connection between the client/server is still alive
 */
@SuppressWarnings("unused")
public class Heartbeat extends RobocolParsableBase  {

  //------------------------------------------------------------------------------------------------
  // Constants
  //------------------------------------------------------------------------------------------------

  public static final short PAYLOAD_SIZE = 8 + 1 + 3*8;

  //------------------------------------------------------------------------------------------------
  // State
  //------------------------------------------------------------------------------------------------

  private long       timestamp;
  private RobotState robotState;
  public  long       t0, t1, t2;    // for time synchronization, a la Network Time Protocol

  //------------------------------------------------------------------------------------------------
  // Construction
  //------------------------------------------------------------------------------------------------

  public Heartbeat() {
    timestamp = 0;
    robotState = RobotState.NOT_STARTED;
    t0 = t1 = t2 = 0;
  }

  public static Heartbeat createWithTimeStamp() {
    Heartbeat result = new Heartbeat();
    result.timestamp = System.nanoTime();
    return result;
    }

  //------------------------------------------------------------------------------------------------
  // Time Synchronization
  //------------------------------------------------------------------------------------------------

  /** returns the time on the clock that is passed in the t0, t1, and t2 state variables */
  public static long getMsTimeSyncTime() {
    return System.currentTimeMillis();
  }

  //------------------------------------------------------------------------------------------------
  // Operations
  //------------------------------------------------------------------------------------------------

  /**
   * Timestamp this Heartbeat was created at
   * <p>
   * Device dependent, cannot compare across devices
   * @return timestamp
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Number of seconds since Heartbeat was created
   * <p>
   * Device dependent, cannot compare across devices
   * @return elapsed time
   */
  public double getElapsedSeconds() {
    return (System.nanoTime() - timestamp) / (double)ElapsedTime.SECOND_IN_NANO;
  }

  /**
   * Get Robocol message type
   * @return RobocolParsable.MsgType.HEARTBEAT
   */
  @Override
  public MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.HEARTBEAT;
  }

  /**
   * Get RobotState
   * @return byte currentState of robot.
   */
  public byte getRobotState() {
    return robotState.asByte();
  }

  /**
   * Set RobotState
   */
  public void setRobotState(RobotState state) {
    robotState = state;
  }

  //------------------------------------------------------------------------------------------------
  // Serialization
  //------------------------------------------------------------------------------------------------

  /**
   * Convert this Heartbeat into a byte array
   */
  @Override
  public byte[] toByteArray() throws RobotCoreException {
    ByteBuffer buffer = getWriteBuffer(PAYLOAD_SIZE);
    try {
      buffer.putLong(timestamp);
      buffer.put(robotState.asByte());
      buffer.putLong(t0);
      buffer.putLong(t1);
      buffer.putLong(t2);
    } catch (BufferOverflowException e) {
      RobotLog.logStacktrace(e);
    }
    return buffer.array();
  }

  /**
   * Populate this Heartbeat from a byte array
   */
  @Override
  public void fromByteArray(byte[] byteArray) throws RobotCoreException {
    try {
      ByteBuffer byteBuffer = getReadBuffer(byteArray);
      timestamp      = byteBuffer.getLong();
      robotState     = RobotState.fromByte(byteBuffer.get());
      t0             = byteBuffer.getLong();
      t1             = byteBuffer.getLong();
      t2             = byteBuffer.getLong();
    } catch (BufferUnderflowException e) {
      throw RobotCoreException.createChained(e, "incoming packet too small");
    }
  }

  //------------------------------------------------------------------------------------------------
  // Pretty Printing
  //------------------------------------------------------------------------------------------------

  /**
   * String containing sequence number and timestamp
   * @return String
   */
  @Override
  public String toString() {
    return String.format("Heartbeat - seq: %4d, time: %d", getSequenceNumber(), timestamp);
  }

}

// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

/**
 * A class that provides JavaScript access to {@link VectorF}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class VectorFAccess extends Access {

  VectorFAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getLength(Object vector) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).length();
      } else {
        RobotLog.e("VectorF.getLength - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.getLength - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float getMagnitude(Object vector) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).magnitude();
      } else {
        RobotLog.e("VectorF.getMagnitude - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.getMagnitude - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF create(int length) {
    checkIfStopRequested();
    try {
      return VectorF.length(length);
    } catch (Exception e) {
      RobotLog.e("VectorF.create - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float get(Object vector, int index) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).get(index);
      } else {
        RobotLog.e("VectorF.get - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.get - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void put(Object vector, int index, float value) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        ((VectorF) vector).put(index, value);
      } else {
        RobotLog.e("VectorF.put - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.put - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object vector) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).toString();
      } else {
        RobotLog.e("VectorF.toText - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.toText - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF normalized3D(Object vector) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).normalized3D();
      } else {
        RobotLog.e("VectorF.normalized3D - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.normalized3D - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float dotProduct(Object vector1, Object vector2) {
    checkIfStopRequested();
    try {
      if (vector1 instanceof VectorF) {
        if (vector2 instanceof VectorF) {
          return ((VectorF) vector1).dotProduct((VectorF) vector2);
        } else {
          RobotLog.e("VectorF.dotProduct - vector2 is not a VectorF");
        }
      } else {
        RobotLog.e("VectorF.dotProduct - vector1 is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.dotProduct - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF multiplied(Object vector, Object matrix) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        if (matrix instanceof MatrixF) {
          return ((VectorF) vector).multiplied((MatrixF) matrix);
        } else {
          RobotLog.e("VectorF.multiplied - matrix is not a MatrixF");
        }
      } else {
        RobotLog.e("VectorF.multiplied - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF added_withMatrix(Object vector, Object matrix) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        if (matrix instanceof MatrixF) {
          return ((VectorF) vector).added((MatrixF) matrix);
        } else {
          RobotLog.e("VectorF.added - matrix is not a MatrixF");
        }
      } else {
        RobotLog.e("VectorF.added - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.added - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF added_withVector(Object vector1, Object vector2) {
    checkIfStopRequested();
    try {
      if (vector1 instanceof VectorF) {
        if (vector2 instanceof VectorF) {
          return ((VectorF) vector1).added((VectorF) vector2);
        } else {
          RobotLog.e("VectorF.added - vector2 is not a VectorF");
        }
      } else {
        RobotLog.e("VectorF.added - vector1 is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.added - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void add_withVector(Object vector1, Object vector2) {
    checkIfStopRequested();
    try {
      if (vector1 instanceof VectorF) {
        if (vector2 instanceof VectorF) {
          ((VectorF) vector1).add((VectorF) vector2);
        } else {
          RobotLog.e("VectorF.add - vector2 is not a VectorF");
        }
      } else {
        RobotLog.e("VectorF.add - vector1 is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.add - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF subtracted_withMatrix(Object vector, Object matrix) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        if (matrix instanceof MatrixF) {
          return ((VectorF) vector).subtracted((MatrixF) matrix);
        } else {
          RobotLog.e("VectorF.subtracted - matrix is not a MatrixF");
        }
      } else {
        RobotLog.e("VectorF.subtracted - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.subtracted - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF subtracted_withVector(Object vector1, Object vector2) {
    checkIfStopRequested();
    try {
      if (vector1 instanceof VectorF) {
        if (vector2 instanceof VectorF) {
          return ((VectorF) vector1).subtracted((VectorF) vector2);
        } else {
          RobotLog.e("VectorF.subtracted - vector2 is not a VectorF");
        }
      } else {
        RobotLog.e("VectorF.subtracted - vector1 is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.subtracted - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void subtract_withVector(Object vector1, Object vector2) {
    checkIfStopRequested();
    try {
      if (vector1 instanceof VectorF) {
        if (vector2 instanceof VectorF) {
          ((VectorF) vector1).subtract((VectorF) vector2);
        } else {
          RobotLog.e("VectorF.subtract - vector2 is not a VectorF");
        }
      } else {
        RobotLog.e("VectorF.subtract - vector1 is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.subtract - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF multiplied_withScale(Object vector, float scale) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return ((VectorF) vector).multiplied(scale);
      } else {
        RobotLog.e("VectorF.multiplied - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void multiply_withScale(Object vector, float scale) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        ((VectorF) vector).multiply(scale);
      } else {
        RobotLog.e("VectorF.multiply - vector is not a VectorF");
      }
    } catch (Exception e) {
      RobotLog.e("VectorF.multiply - caught " + e);
    }
  }
}
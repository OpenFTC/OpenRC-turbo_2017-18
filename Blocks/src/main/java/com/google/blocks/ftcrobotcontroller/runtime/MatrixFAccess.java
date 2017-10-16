// Copyright 2016 Google Inc.

package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * A class that provides JavaScript access to {@link MatrixF}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class MatrixFAccess extends Access {

  MatrixFAccess(BlocksOpMode blocksOpMode, String identifier) {
    super(blocksOpMode, identifier);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getNumRows(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).numRows();
      } else {
        RobotLog.e("MatrixF.getNumRows - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.getNumRows - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getNumCols(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).numCols();
      } else {
        RobotLog.e("MatrixF.getNumCols - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.getNumCols - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF slice(Object matrix, int row, int col, int numRows, int numCols) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).slice(row, col, numRows, numCols);
      } else {
        RobotLog.e("MatrixF.slice - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.slice - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF identityMatrix(int dim) {
    checkIfStopRequested();
    try {
      return MatrixF.identityMatrix(dim);
    } catch (Exception e) {
      RobotLog.e("MatrixF.identityMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF diagonalMatrix(int dim, int scale) {
    checkIfStopRequested();
    try {
      return MatrixF.diagonalMatrix(dim, scale);
    } catch (Exception e) {
      RobotLog.e("MatrixF.diagonalMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF diagonalMatrix_withVector(Object vector) {
    checkIfStopRequested();
    try {
      if (vector instanceof VectorF) {
        return MatrixF.diagonalMatrix((VectorF) vector);
      } else {
        RobotLog.e("MatrixF.diagonalMatrix - vector is not a VectorV");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.diagonalMatrix - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public float get(Object matrix, int row, int col) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).get(row, col);
      } else {
        RobotLog.e("MatrixF.get - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.get - caught " + e);
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void put(Object matrix, int row, int col, float value) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        ((MatrixF) matrix).put(row, col, value);
      } else {
        RobotLog.e("MatrixF.put - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.put - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF getRow(Object matrix, int row) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).getRow(row);
      } else {
        RobotLog.e("MatrixF.getRow - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.getRow - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF getColumn(Object matrix, int col) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).getColumn(col);
      } else {
        RobotLog.e("MatrixF.getColumn - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.getColumn - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String toText(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).toString();
      } else {
        RobotLog.e("MatrixF.toText - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.toText - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF transform(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          return ((MatrixF) matrix).transform((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.transform - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.transform - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.transform - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String formatAsTransform(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).formatAsTransform();
      } else {
        RobotLog.e("MatrixF.formatAsTransform - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.formatAsTransform - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public String formatAsTransform_withArgs(
      Object matrix, String axesReferenceString, String axesOrderString, String angleUnitString) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        AxesReference axesReference = AxesReference.valueOf(axesReferenceString.toUpperCase(Locale.ENGLISH));
        AxesOrder axesOrder = AxesOrder.valueOf(axesOrderString.toUpperCase(Locale.ENGLISH));
        AngleUnit angleUnit = AngleUnit.valueOf(angleUnitString.toUpperCase(Locale.ENGLISH));
        return ((MatrixF) matrix).formatAsTransform(axesReference, axesOrder, angleUnit);
      } else {
        RobotLog.e("MatrixF.formatAsTransform - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.formatAsTransform - caught " + e);
    }
    return "";
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF transposed(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).transposed();
      } else {
        RobotLog.e("MatrixF.transposed - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.transposed - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF multiplied_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
            return ((MatrixF) matrix1).multiplied((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.multiplied - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.multiplied - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF multiplied_withScale(Object matrix, float scale) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).multiplied(scale);
      } else {
        RobotLog.e("MatrixF.multiplied - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF multiplied_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          return ((MatrixF) matrix).multiplied((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.multiplied - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.multiplied - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiplied - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void multiply_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
          ((MatrixF) matrix1).multiply((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.multiply - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.multiply - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiply - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void multiply_withScale(Object matrix, float scale) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        ((MatrixF) matrix).multiply(scale);
      } else {
        RobotLog.e("MatrixF.multiply - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiply - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void multiply_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          ((MatrixF) matrix).multiply((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.multiply - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.multiply - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.multiply - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF toVector(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).toVector();
      } else {
        RobotLog.e("MatrixF.toVector - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.toVector - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF added_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
          return ((MatrixF) matrix1).added((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.added - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.added - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.added - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF added_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          return ((MatrixF) matrix).added((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.added - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.added - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.added - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void add_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
          ((MatrixF) matrix1).add((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.add - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.add - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.add - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void add_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          ((MatrixF) matrix).add((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.add - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.add - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.add - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF subtracted_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
          return ((MatrixF) matrix1).subtracted((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.subtracted - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.subtracted - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.subtracted - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF subtracted_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          return ((MatrixF) matrix).subtracted((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.subtracted - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.subtracted - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.subtracted - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void subtract_withMatrix(Object matrix1, Object matrix2) {
    checkIfStopRequested();
    try {
      if (matrix1 instanceof MatrixF) {
        if (matrix2 instanceof MatrixF) {
          ((MatrixF) matrix1).subtract((MatrixF) matrix2);
        } else {
          RobotLog.e("MatrixF.subtract - matrix2 is not a MatrixF");
        }
      } else {
        RobotLog.e("MatrixF.subtract - matrix1 is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.subtract - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void subtract_withVector(Object matrix, Object vector) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        if (vector instanceof VectorF) {
          ((MatrixF) matrix).subtract((VectorF) vector);
        } else {
          RobotLog.e("MatrixF.subtract - vector is not a VectorF");
        }
      } else {
        RobotLog.e("MatrixF.subtract - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.subtract - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public VectorF getTranslation(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).getTranslation();
      } else {
        RobotLog.e("MatrixF.getTranslation - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.getTranslation - caught " + e);
    }
    return null;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public MatrixF inverted(Object matrix) {
    checkIfStopRequested();
    try {
      if (matrix instanceof MatrixF) {
        return ((MatrixF) matrix).inverted();
      } else {
        RobotLog.e("MatrixF.inverted - matrix is not a MatrixF");
      }
    } catch (Exception e) {
      RobotLog.e("MatrixF.inverted - caught " + e);
    }
    return null;
  }
}

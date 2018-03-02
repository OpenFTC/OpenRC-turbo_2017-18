//package org.firstinspires.ftc.teamcode.teleop.test;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.hardwaremap.RevbotMecanum;
//
//import me.joshlin.a3565lib.component.encodermotor.EncoderMotorHardwareComponent;
//import me.joshlin.a3565lib.component.encodermotor.EncoderMotorPivot;
//import me.joshlin.a3565lib.component.interfaces.Pivot;
//
///**
// * Created by josh on 2/17/18.
// */
//
//@TeleOp(name = "Winch Test", group = "conventional")
//public class PivotTest extends LinearOpMode {
//    private RevbotMecanum robot = new RevbotMecanum();
//
//    private Pivot flipper;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        robot.init(hardwareMap);
//        flipper = robot.flipper;
//        flipper.
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            if (gamepad1.dpad_up) {
//                flipper.setPosition(flipper.getPosition() + 10);
//            } else if (gamepad1.dpad_down) {
//                flipper.setPosition(flipper.getPosition() - 10);
//            }
//
//            telemetry.addData("Position", flipper.getPosition());
//            telemetry.update();
//
//            sleep(100);
//        }
//    }
//}
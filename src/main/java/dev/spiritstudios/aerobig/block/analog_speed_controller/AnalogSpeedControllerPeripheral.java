package dev.spiritstudios.aerobig.block.analog_speed_controller;

import com.simibubi.create.compat.computercraft.implementation.peripherals.SyncedPeripheral;
import dev.spiritstudios.aerobig.BigAircraft;
import org.jetbrains.annotations.NotNull;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import dan200.computercraft.api.lua.LuaFunction;

public class AnalogSpeedControllerPeripheral extends SyncedPeripheral<AnalogSpeedControllerBlockEntity> {

	private final ScrollValueBehaviour targetSpeed;

	public AnalogSpeedControllerPeripheral(AnalogSpeedControllerBlockEntity blockEntity, ScrollValueBehaviour targetSpeed) {
		super(blockEntity);
		this.targetSpeed = targetSpeed;
	}

	@LuaFunction(mainThread = true)
	public final void setTargetSpeed(int speed) {
		this.targetSpeed.setValue(speed);
	}

	@LuaFunction
	public final float getTargetSpeed() {
		return this.targetSpeed.getValue();
	}

	@NotNull
	@Override
	public String getType() {
		return BigAircraft.MOD_NAME.replace(" ", "") + "_AnalogSpeedController";
	}

}

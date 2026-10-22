package org.eqasim.core.simulation.modes.drt.mode_choice.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.ReflectiveConfigGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DrtMaxDistanceConstraintConfigGroup extends ReflectiveConfigGroup {

    public final static String GROUP_NAME = "eqasim:drtMaxDistanceConstraint";

    public DrtMaxDistanceConstraintConfigGroup() {
        super(GROUP_NAME);
    }

    @Override
    public ConfigGroup createParameterSet(String type) {
        if(ModeMaxDistanceConfigGroup.GROUP_NAME.equals(type)) {
            return new ModeMaxDistanceConfigGroup();
        }
        throw new IllegalStateException(String.format("Parameter set '%s' not supported", type));
    }

    @Override
    public void addParameterSet(ConfigGroup set) {
        if(set instanceof ModeMaxDistanceConfigGroup) {
            super.addParameterSet(set);
        } else {
            throw new IllegalStateException("Unsupported parameter set class: " + set);
        }
    }

    @Override
    protected void checkConsistency(Config config) {
        super.checkConsistency(config);
        getMaxBeelineDistanceByMode();
    }

    public static DrtMaxDistanceConstraintConfigGroup getOrCreate(Config config) {
        DrtMaxDistanceConstraintConfigGroup configGroup = (DrtMaxDistanceConstraintConfigGroup) config.getModules().get(GROUP_NAME);
        if(configGroup == null) {
            configGroup = new DrtMaxDistanceConstraintConfigGroup();
            config.addModule(configGroup);
        }
        return configGroup;
    }

    public void setMaxBeelineDistance(String mode, double maxBeelineDistance) {
        for(ConfigGroup parameterSet: getParameterSets(ModeMaxDistanceConfigGroup.GROUP_NAME)) {
            ModeMaxDistanceConfigGroup modeMaxDistanceConfigGroup = (ModeMaxDistanceConfigGroup) parameterSet;
            if(modeMaxDistanceConfigGroup.mode.equals(mode)) {
                modeMaxDistanceConfigGroup.maxBeelineDistance = maxBeelineDistance;
                return;
            }
        }
        ModeMaxDistanceConfigGroup modeMaxDistanceConfigGroup = new ModeMaxDistanceConfigGroup();
        modeMaxDistanceConfigGroup.mode = mode;
        modeMaxDistanceConfigGroup.maxBeelineDistance = maxBeelineDistance;
        addParameterSet(modeMaxDistanceConfigGroup);
    }

    public Map<String, Double> getMaxBeelineDistanceByMode() {
        Map<String, Double> maxBeelineDistanceByMode = new HashMap<>();
        Collection<? extends ConfigGroup> parameterSets = getParameterSets(ModeMaxDistanceConfigGroup.GROUP_NAME);
        for(ConfigGroup parameterSet: parameterSets) {
            if(!(parameterSet instanceof ModeMaxDistanceConfigGroup modeMaxDistanceConfigGroup)) {
                throw new IllegalStateException("This should not happen");
            }
            if(maxBeelineDistanceByMode.put(modeMaxDistanceConfigGroup.mode, modeMaxDistanceConfigGroup.maxBeelineDistance) != null) {
                throw new IllegalStateException(String.format("The mode %s appears more than once in the %s module config",
                        modeMaxDistanceConfigGroup.mode, GROUP_NAME));
            }
        }
        return maxBeelineDistanceByMode;
    }

    public static class ModeMaxDistanceConfigGroup extends ReflectiveConfigGroup {

        public static final String GROUP_NAME = "modeMaxDistance";

        @Parameter
        @Comment("Drt mode to which the maximum distance applies")
        public @NotBlank String mode;

        @Parameter
        @Comment("Maximum beeline distance [m] between origin and destination activity for which the drt mode is available")
        public @PositiveOrZero double maxBeelineDistance = Double.POSITIVE_INFINITY;

        public ModeMaxDistanceConfigGroup() {
            super(GROUP_NAME);
        }
    }
}

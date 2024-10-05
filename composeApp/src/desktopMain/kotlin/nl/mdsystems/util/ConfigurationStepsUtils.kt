package nl.mdsystems.util

import nl.mdsystems.domain.enums.ConfigurationSteps
import nl.mdsystems.model.Configuration

fun ConfigurationSteps.isEnabled(config: Configuration) : Boolean {
    return packageTypes.contains(config.packageInfo.type) && appImage?.let { it == config.packageVariables.inputIsAppImage } ?: true
}
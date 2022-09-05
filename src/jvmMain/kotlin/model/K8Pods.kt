package model

import kotlinx.serialization.Serializable

@Serializable
data class K8Pods(
    val apiVersion: String,
    val items: List<K8Pod>
)

@Serializable
data class K8Pod(
    val metadata: K8PodMetadata,
    val spec: K8PodSpec,
    val status: K8PodStatus
)

@Serializable
data class K8PodContainer(
    val command: List<String>? = null,
    val image: String,
    val env: List<K8Env>? = null
)

@Serializable
data class K8PodMetadata(
    val name: String,
    val namespace: String
)

@Serializable
data class K8PodSpec(
    val containers: List<K8PodContainer>
)

@Serializable
data class K8Env(
    val name:String,
    val value:String? = null,
    val valueFrom: K8EnvFrom? = null
)

@Serializable
data class K8EnvFrom(
    val secretKeyRef: K8EnvSecretRef? = null,
    val fieldRef: Map<String, String>? = null
)

@Serializable
data class K8EnvSecretRef(
    val key:String,
    val name:String,
    val optional:Boolean? = null
)

@Serializable
data class K8PodStatus(
    val containerStatuses: List<K8PodContainerStatus>,
    val phase: String,
    val startTime: String
)

@Serializable
data class K8PodContainerStatus(
    val image: String,
    val name: String,
    val ready: Boolean,
    val restartCount: Int,
    val started: Boolean
)

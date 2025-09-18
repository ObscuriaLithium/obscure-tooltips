package dev.obscuria.tooltips.client

import com.google.gson.JsonParser
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

object TooltipStyleManager : ResourceManagerReloadListener {

    override fun onResourceManagerReload(manager: ResourceManager) {
        for (kind in ResourceKind.entries) {
            kind.spec.onReloadStart()
            val resources = manager.listResources(kind.spec.resourceDir(), ::isValidResource)
            resources.forEach { (path, resource) -> loadResource(kind, path, resource) }
            kind.spec.onReloadEnd()
        }
    }

    private fun isValidResource(path: ResourceLocation): Boolean {
        return path.toString().endsWith(".json")
    }

    private fun loadResource(kind: ResourceKind, path: ResourceLocation, resource: Resource) {
        kind.spec.load(extractId(kind, path), JsonParser.parseReader(resource.openAsReader()))
    }

    private fun extractId(kind: ResourceKind, path: ResourceLocation): ResourceLocation {
        return path.withPath { it.removePrefix("${kind.spec.resourceDir()}/").removeSuffix(".json") }
    }
}
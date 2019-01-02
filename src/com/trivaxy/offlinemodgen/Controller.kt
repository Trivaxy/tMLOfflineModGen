package com.trivaxy.offlinemodgen

import tornadofx.Controller
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths

class Controller : Controller() {
    companion object {
        var DIRECTORY: String = ""
        var MOD_NAME: String = ""
        var MOD_VERSION: String = "0.1"
        var MOD_DISPLAY_NAME: String = ""
        var MOD_AUTHOR: String = ""
        var MOD_LANGUAGE_VERSION: String = ""
        var GIT_IGNORE = ""

        const val MOD_CLASS_TEMPLATE: String =
"""using Terraria.ModLoader;

namespace [MOD_NAME]
{
  class [MOD_NAME] : Mod
  {
    public [MOD_NAME]()
    {
    }
  }
}
"""
        const val MOD_CSPROJ_TEMPLATE: String =
"""<?xml version="1.0" encoding="utf-8"?>
<Project ToolsVersion="14.0" DefaultTargets="Build" xmlns="http://schemas.microsoft.com/developer/msbuild/2003">
  <Import Project="${'$'}(MSBuildExtensionsPath)${'$'}(MSBuildToolsVersion)\Microsoft.Common.props" Condition="Exists('${'$'}(MSBuildExtensionsPath)${'$'}(MSBuildToolsVersion)\Microsoft.Common.props')" />
  <PropertyGroup>
    <Configuration Condition=" '${'$'}(Configuration)' == '' ">Debug</Configuration>
    <Platform Condition=" '${'$'}(Platform)' == '' ">AnyCPU</Platform>
    <ProjectGuid>{8298EAB6-0586-4BDA-9483-83624B66B13A}</ProjectGuid>
    <OutputType>Library</OutputType>
    <AppDesignerFolder>Properties</AppDesignerFolder>
    <RootNamespace>[MOD_NAME]</RootNamespace>
    <AssemblyName>[MOD_NAME]</AssemblyName>
    <TargetFrameworkVersion>v4.5.2</TargetFrameworkVersion>
    <FileAlignment>512</FileAlignment>
    <AutoGenerateBindingRedirects>true</AutoGenerateBindingRedirects>
  </PropertyGroup>
  <PropertyGroup Condition=" '${'$'}(Configuration)|${'$'}(Platform)' == 'Debug|AnyCPU' ">
    <PlatformTarget>AnyCPU</PlatformTarget>
    <DebugSymbols>true</DebugSymbols>
    <DebugType>full</DebugType>
    <Optimize>false</Optimize>
    <OutputPath>bin\Debug\</OutputPath>
    <DefineConstants>DEBUG;TRACE</DefineConstants>
    <ErrorReport>prompt</ErrorReport>
    <WarningLevel>4</WarningLevel>
  </PropertyGroup>
  <PropertyGroup Condition=" '${'$'}(Configuration)|${'$'}(Platform)' == 'Release|AnyCPU' ">
    <PlatformTarget>AnyCPU</PlatformTarget>
    <DebugType>pdbonly</DebugType>
    <Optimize>true</Optimize>
    <OutputPath>bin\Release\</OutputPath>
    <DefineConstants>TRACE</DefineConstants>
    <ErrorReport>prompt</ErrorReport>
    <WarningLevel>4</WarningLevel>
  </PropertyGroup>
  <ItemGroup>
    <Compile Include="**\*.cs" Exclude="obj\**\*.*" />
  </ItemGroup>
  <ItemGroup>
    <Reference Include="Microsoft.Xna.Framework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=842cf8be1de50553, processorArchitecture=x86">
      <SpecificVersion>False</SpecificVersion>
      <HintPath>C:\Windows\Microsoft.NET\assembly\GAC_32\Microsoft.Xna.Framework\v4.0_4.0.0.0__842cf8be1de50553\Microsoft.Xna.Framework.dll</HintPath>
    </Reference>
    <Reference Include="Microsoft.Xna.Framework.Game, Version=4.0.0.0, Culture=neutral, PublicKeyToken=842cf8be1de50553, processorArchitecture=x86">
      <SpecificVersion>False</SpecificVersion>
      <HintPath>C:\Windows\Microsoft.NET\assembly\GAC_32\Microsoft.Xna.Framework.Game\v4.0_4.0.0.0__842cf8be1de50553\Microsoft.Xna.Framework.Game.dll</HintPath>
    </Reference>
    <Reference Include="Microsoft.Xna.Framework.Graphics, Version=4.0.0.0, Culture=neutral, PublicKeyToken=842cf8be1de50553, processorArchitecture=x86">
      <SpecificVersion>False</SpecificVersion>
      <HintPath>C:\Windows\Microsoft.NET\assembly\GAC_32\Microsoft.Xna.Framework.Graphics\v4.0_4.0.0.0__842cf8be1de50553\Microsoft.Xna.Framework.Graphics.dll</HintPath>
    </Reference>
    <Reference Include="Microsoft.Xna.Framework.Xact, Version=4.0.0.0, Culture=neutral, PublicKeyToken=842cf8be1de50553, processorArchitecture=x86">
      <SpecificVersion>False</SpecificVersion>
      <HintPath>C:\Windows\Microsoft.NET\assembly\GAC_32\Microsoft.Xna.Framework.Xact\v4.0_4.0.0.0__842cf8be1de50553\Microsoft.Xna.Framework.Xact.dll</HintPath>
    </Reference>
    <Reference Include="System" />
    <Reference Include="Terraria">
      <HintPath>C:\Program Files (x86)\Steam\steamapps\common\terraria\Terraria.exe</HintPath>
    </Reference>
  </ItemGroup>
  <Import Project="${'$'}(MSBuildToolsPath)\Microsoft.CSharp.targets" />
  <PropertyGroup>
   <PostBuildEvent>"C:\Program Files (x86)\Steam\steamapps\common\terraria\tModLoaderServer.exe" -build "${'$'}(ProjectDir)\" -eac "${'$'}(TargetPath)"</PostBuildEvent>
  </PropertyGroup>
</Project>
"""
        const val MOD_CSPROJ_USER_TEMPLATE =
"""<?xml version="1.0" encoding="utf-8"?>
<Project ToolsVersion="14.0" xmlns="http://schemas.microsoft.com/developer/msbuild/2003">
  <PropertyGroup Condition="'${'$'}(Configuration)|${'$'}(Platform)' == 'Debug|AnyCPU'">
    <StartAction>Program</StartAction>
    <StartProgram>C:\Program Files %28x86%29\Steam\steamapps\common\Terraria\Terraria.exe</StartProgram>
    <StartWorkingDirectory>C:\Program Files %28x86%29\Steam\steamapps\common\Terraria\</StartWorkingDirectory>
  </PropertyGroup>
</Project>
"""

        fun generateModSkeleton() {
            if (getStatusMessage().startsWith("Status: OK!")) {
                var path = Paths.get(DIRECTORY, MOD_NAME)

                //initialize the writers, write build.txt
                File(path.toUri()).mkdir()
                var fileWriter = FileWriter(Paths.get(path.toString(), "build.txt").toString())
                var writer = PrintWriter(fileWriter, true)
                writer.println("author = " + MOD_AUTHOR)
                writer.println("version = " + MOD_VERSION)
                writer.println("displayName = " + MOD_DISPLAY_NAME)
                if (MOD_LANGUAGE_VERSION.isNotEmpty()) writer.println("languageVersion = " + MOD_LANGUAGE_VERSION)

                fileWriter.close()

                //write description.txt
                fileWriter = FileWriter(Paths.get(path.toString(), "description.txt").toString())
                writer = PrintWriter(fileWriter, true)
                writer.println(MOD_DISPLAY_NAME + " is a pretty cool mod, it does.. this. Modify this file with a description of your mod.")
                fileWriter.close()

                //write MODNAME.cs
                fileWriter = FileWriter(Paths.get(path.toString(), MOD_NAME + ".cs").toString())
                writer = PrintWriter(fileWriter, true)
                writer.write(MOD_CLASS_TEMPLATE.replace("[MOD_NAME]", MOD_NAME))
                fileWriter.close()

                //write MODNAME.csproj
                fileWriter = FileWriter(Paths.get(path.toString(), MOD_NAME + ".csproj").toString())
                writer = PrintWriter(fileWriter, true)
                writer.write(MOD_CSPROJ_TEMPLATE.replace("[MOD_NAME]", MOD_NAME))
                fileWriter.close()

                //write MODNAME.csproj.user
                fileWriter = FileWriter(Paths.get(path.toString(), MOD_NAME + ".csproj.user").toString())
                writer = PrintWriter(fileWriter, true)
                writer.write(MOD_CSPROJ_USER_TEMPLATE)
                fileWriter.close()

                //write .gitignore
                fileWriter = FileWriter(Paths.get(path.toString(), ".gitignore").toString())
                writer = PrintWriter(fileWriter, true)
                if (GIT_IGNORE.trim().isEmpty()) {
                    writer.println("*.csproj")
                    writer.println("*.user")
                    writer.println("*.sln")
                    writer.println("bin/")
                    writer.println("obj/")
                }
                else {
                    var ignores = GIT_IGNORE.split(Regex("""\,\s*"""))
                    for (i in ignores) {
                        writer.println(i)
                    }
                }

                writer.close()
                fileWriter.close()
            }
        }

        fun getStatusMessage(): String {
            if (Files.notExists(Paths.get(DIRECTORY)) || DIRECTORY == "") return "Error: Directory invalid."
            if (MOD_NAME.contains(" ")) return "Error: Mod name has spaces."
            if (MOD_NAME.isEmpty()) return "Error: No mod name specified."
            if (MOD_DISPLAY_NAME.isEmpty()) return "Error: No mod display name specified."
            if (!MOD_VERSION.matches(Regex("""([0-9]*\.[0-9]+)+"""))) return "Error: Invalid mod version."
            if (MOD_AUTHOR.isEmpty()) return "Error: No author specified."
            if (!MOD_LANGUAGE_VERSION.matches(Regex("""[4-6]""")) && MOD_LANGUAGE_VERSION.isNotEmpty()) return "Error: Language version must be a whole number from 4 to 6."

            return "Status: OK! Mod generated!"
        }
    }
}
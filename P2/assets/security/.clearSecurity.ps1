$scriptName = Split-Path -Path $PSCommandPath -Leaf
git -C $PSScriptRoot clean -fdX -- . ":(exclude)$scriptName"
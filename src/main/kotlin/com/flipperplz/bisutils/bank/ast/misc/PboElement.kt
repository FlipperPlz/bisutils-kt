package com.flipperplz.bisutils.bank.ast.misc

import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.utils.BisFamily

interface PboElement : BisFamily {
    override val lowestBranch: PboFile?
}
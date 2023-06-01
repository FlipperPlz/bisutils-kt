import com.flipperplz.bisutils.bank.ast.PboEntry
import com.flipperplz.bisutils.bank.ast.PboFile
import com.flipperplz.bisutils.bank.utils.EntryMimeType
import com.flipperplz.bisutils.utils.BisFamily
import com.flipperplz.bisutils.utils.BisStrictDebinarizable
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class PboEntryImpl(
    override val lowestBranch: PboFile?,
    override val parent: BisFamily?,
    override val entryName: String,
    override val entryMime: EntryMimeType,
    override val entryDecompressedSize: Long,
    override val entryOffset: Long,
    override val entryTimestamp: Long,
    override val entrySize: Long
) : BisStrictDebinarizable(), PboEntry {

    override fun read(buffer: ByteBuffer, charset: Charset) {
        TODO("Not yet implemented")
    }
}
package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.ChatMessage;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ArgumentNBTKey implements ArgumentType<ArgumentNBTKey.h> {
	public static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new ChatMessage("arguments.nbtpath.node.invalid"));
	public static final DynamicCommandExceptionType b = new DynamicCommandExceptionType((object) -> new ChatMessage("arguments.nbtpath.nothing_found", object));
	private static final Collection<String> c = Arrays.asList("foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}");

	public ArgumentNBTKey() {}

	public static ArgumentNBTKey a() {
		return new ArgumentNBTKey();
	}

	public static ArgumentNBTKey.h a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
		return commandcontext.getArgument(s, h.class);
	}

	private static ArgumentNBTKey.i a(StringReader stringreader, boolean flag) throws CommandSyntaxException {
		String s;

		switch (stringreader.peek()) {
			case '"' -> {
				s = stringreader.readString();
				return a(stringreader, s);
			}
			case '[' -> {
				stringreader.skip();
				char c0 = stringreader.peek();
				if (c0 == '{') {
					NBTTagCompound nbttagcompound = (new MojangsonParser(stringreader)).f();

					stringreader.expect(']');
					return new e(nbttagcompound);
				} else {
					if (c0 == ']') {
						stringreader.skip();
						return ArgumentNBTKey.a_.a;
					}

					int i = stringreader.readInt();

					stringreader.expect(']');
					return new c(i);
				}
			}
			case '{' -> {
				if (!flag) {
					throw ArgumentNBTKey.a.createWithContext(stringreader);
				}
				NBTTagCompound nbttagcompound1 = (new MojangsonParser(stringreader)).f();
				return new g(nbttagcompound1);
			}
			default -> {
				s = b(stringreader);
				return a(stringreader, s);
			}
		}
	}

	private static ArgumentNBTKey.i a(StringReader stringreader, String s) throws CommandSyntaxException {
		if (stringreader.canRead() && stringreader.peek() == '{') {
			NBTTagCompound nbttagcompound = (new MojangsonParser(stringreader)).f();

			return new ArgumentNBTKey.f(s, nbttagcompound);
		} else {
			return new ArgumentNBTKey.b(s);
		}
	}

	private static String b(StringReader stringreader) throws CommandSyntaxException {
		int i = stringreader.getCursor();

		while (stringreader.canRead() && a(stringreader.peek())) {
			stringreader.skip();
		}

		if (stringreader.getCursor() == i) {
			throw ArgumentNBTKey.a.createWithContext(stringreader);
		} else {
			return stringreader.getString().substring(i, stringreader.getCursor());
		}
	}

	private static boolean a(char c0) {
		return c0 != ' ' && c0 != '"' && c0 != '[' && c0 != ']' && c0 != '.' && c0 != '{' && c0 != '}';
	}

	private static Predicate<NBTBase> b(NBTTagCompound nbttagcompound) {
		return (nbtbase) -> GameProfileSerializer.a(nbttagcompound, nbtbase, true);
	}

	public ArgumentNBTKey.h parse(StringReader stringreader) throws CommandSyntaxException {
		List<ArgumentNBTKey.i> list = Lists.newArrayList();
		int i = stringreader.getCursor();
		Object2IntMap<ArgumentNBTKey.i> object2intmap = new Object2IntOpenHashMap<>();
		boolean flag = true;

		while (stringreader.canRead() && stringreader.peek() != ' ') {
			ArgumentNBTKey.i argumentnbtkey_i = a(stringreader, flag);

			list.add(argumentnbtkey_i);
			object2intmap.put(argumentnbtkey_i, stringreader.getCursor() - i);
			flag = false;
			if (stringreader.canRead()) {
				char c0 = stringreader.peek();

				if (c0 != ' ' && c0 != '[' && c0 != '{') {
					stringreader.expect('.');
				}
			}
		}

		return new ArgumentNBTKey.h(stringreader.getString().substring(i, stringreader.getCursor()), list.toArray(new i[0]), object2intmap);
	}

	public Collection<String> getExamples() {
		return ArgumentNBTKey.c;
	}

	public interface i {
		void a(NBTBase nbtbase, List<NBTBase> list);

		void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list);

		NBTBase a();

		int a(NBTBase nbtbase, Supplier<NBTBase> supplier);

		int a(NBTBase nbtbase);

		default List<NBTBase> a(List<NBTBase> list) {
			BiConsumer<NBTBase, List<NBTBase>> fn = this::a;
			return this.a(list, fn);
		}

		default List<NBTBase> a(List<NBTBase> list, Supplier<NBTBase> supplier) {
			return this.a(list, (nbtbase, list1) -> this.a(nbtbase, supplier, list1));
		}

		default List<NBTBase> a(List<NBTBase> list, BiConsumer<NBTBase, List<NBTBase>> biconsumer) {
			List<NBTBase> list1 = Lists.newArrayList();

			for (NBTBase nbtbase : list) {
				biconsumer.accept(nbtbase, list1);
			}

			return list1;
		}
	}

	static class g implements ArgumentNBTKey.i {

		private final Predicate<NBTBase> a;

		public g(NBTTagCompound nbttagcompound) {
			this.a = ArgumentNBTKey.b(nbttagcompound);
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagCompound && this.a.test(nbtbase)) {
				list.add(nbtbase);
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			this.a(nbtbase, list);
		}

		@Override
		public NBTBase a() {
			return new NBTTagCompound();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			return 0;
		}

		@Override
		public int a(NBTBase nbtbase) {
			return 0;
		}
	}

	static class f implements ArgumentNBTKey.i {

		private final String a;
		private final NBTTagCompound b;
		private final Predicate<NBTBase> c;

		public f(String s, NBTTagCompound nbttagcompound) {
			this.a = s;
			this.b = nbttagcompound;
			this.c = ArgumentNBTKey.b(nbttagcompound);
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagCompound) {
				NBTBase nbtbase1 = ((NBTTagCompound) nbtbase).get(this.a);

				if (this.c.test(nbtbase1)) {
					list.add(nbtbase1);
				}
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {
				NBTBase nbtbase1 = nbttagcompound.get(this.a);

				if (nbtbase1 == null) {
					NBTTagCompound nbttagcompound1 = this.b.clone();

					nbttagcompound.set(this.a, nbttagcompound1);
					list.add(nbttagcompound1);
				} else if (this.c.test(nbtbase1)) {
					list.add(nbtbase1);
				}
			}

		}

		@Override
		public NBTBase a() {
			return new NBTTagCompound();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {
				NBTBase nbtbase1 = nbttagcompound.get(this.a);

				if (this.c.test(nbtbase1)) {
					NBTBase nbtbase2 = supplier.get();

					if (!nbtbase2.equals(nbtbase1)) {
						nbttagcompound.set(this.a, nbtbase2);
						return 1;
					}
				}
			}

			return 0;
		}

		@Override
		public int a(NBTBase nbtbase) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {
				NBTBase nbtbase1 = nbttagcompound.get(this.a);

				if (this.c.test(nbtbase1)) {
					nbttagcompound.remove(this.a);
					return 1;
				}
			}

			return 0;
		}
	}

	static class a_ implements ArgumentNBTKey.i {

		public static final a_ a = new a_();

		private a_() {
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTList<?> nbtList) {
				list.addAll(nbtList);
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			if (nbtbase instanceof NBTList<?> nbtlist) {
				if (nbtlist.isEmpty()) {
					NBTBase nbtbase1 = supplier.get();

					if (nbtlist.b(0, nbtbase1)) {
						list.add(nbtbase1);
					}
				} else {
					list.addAll(nbtlist);
				}
			}

		}

		@Override
		public NBTBase a() {
			return new NBTTagList();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			if (!(nbtbase instanceof NBTList<?> nbtlist)) {
				return 0;
			} else {
				int i = nbtlist.size();

				if (i == 0) {
					nbtlist.b(0, supplier.get());
					return 1;
				} else {
					NBTBase nbtbase1 = supplier.get();

					int j = i - (int) nbtlist.stream().filter(nbtbase1::equals).count();

					if (j == 0) {
						return 0;
					} else {
						nbtlist.clear();
						if (!nbtlist.b(0, nbtbase1)) {
							return 0;
						} else {
							for (int k = 1; k < i; ++k) {
								nbtlist.b(k, supplier.get());
							}

							return j;
						}
					}
				}
			}
		}

		@Override
		public int a(NBTBase nbtbase) {
			if (nbtbase instanceof NBTList<?> nbtlist) {
				int i = nbtlist.size();

				if (i > 0) {
					nbtlist.clear();
					return i;
				}
			}

			return 0;
		}
	}

	static class e implements ArgumentNBTKey.i {
		private final NBTTagCompound a;
		private final Predicate<NBTBase> b;

		public e(NBTTagCompound nbttagcompound) {
			this.a = nbttagcompound;
			this.b = ArgumentNBTKey.b(nbttagcompound);
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagList nbttaglist) {

				nbttaglist.stream().filter(this.b).forEach(list::add);
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			MutableBoolean mutableboolean = new MutableBoolean();

			if (nbtbase instanceof NBTTagList nbttaglist) {

				nbttaglist.stream().filter(this.b).forEach((nbtbase1) -> {
					list.add(nbtbase1);
					mutableboolean.setTrue();
				});
				if (mutableboolean.isFalse()) {
					NBTTagCompound nbttagcompound = this.a.clone();

					nbttaglist.add(nbttagcompound);
					list.add(nbttagcompound);
				}
			}

		}

		@Override
		public NBTBase a() {
			return new NBTTagList();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			int i = 0;

			if (nbtbase instanceof NBTTagList nbttaglist) {
				int j = nbttaglist.size();

				if (j == 0) {
					nbttaglist.add(supplier.get());
					++i;
				} else {
					for (int k = 0; k < j; ++k) {
						NBTBase nbtbase1 = nbttaglist.get(k);

						if (this.b.test(nbtbase1)) {
							NBTBase nbtbase2 = supplier.get();

							if (!nbtbase2.equals(nbtbase1) && nbttaglist.a(k, nbtbase2)) {
								++i;
							}
						}
					}
				}
			}

			return i;
		}

		@Override
		public int a(NBTBase nbtbase) {
			int i = 0;

			if (nbtbase instanceof NBTTagList nbttaglist) {

				for (int j = nbttaglist.size() - 1; j >= 0; --j) {
					if (this.b.test(nbttaglist.get(j))) {
						nbttaglist.remove(j);
						++i;
					}
				}
			}

			return i;
		}
	}

	static class c implements ArgumentNBTKey.i {

		private final int a;

		public c(int i) {
			this.a = i;
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTList<?> nbtlist) {
				int i = nbtlist.size();
				int j = this.a < 0 ? i + this.a : this.a;

				if (0 <= j && j < i) {
					list.add(nbtlist.get(j));
				}
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			this.a(nbtbase, list);
		}

		@Override
		public NBTBase a() {
			return new NBTTagList();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			if (nbtbase instanceof NBTList<?> nbtlist) {
				int i = nbtlist.size();
				int j = this.a < 0 ? i + this.a : this.a;

				if (0 <= j && j < i) {
					NBTBase nbtbase1 = nbtlist.get(j);
					NBTBase nbtbase2 = supplier.get();

					if (!nbtbase2.equals(nbtbase1) && nbtlist.a(j, nbtbase2)) {
						return 1;
					}
				}
			}

			return 0;
		}

		@Override
		public int a(NBTBase nbtbase) {
			if (nbtbase instanceof NBTList<?> nbtlist) {
				int i = nbtlist.size();
				int j = this.a < 0 ? i + this.a : this.a;

				if (0 <= j && j < i) {
					nbtlist.remove(j);
					return 1;
				}
			}

			return 0;
		}
	}

	static class b implements ArgumentNBTKey.i {
		private final String a;

		public b(String s) {
			this.a = s;
		}

		@Override
		public void a(NBTBase nbtbase, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagCompound) {
				NBTBase nbtbase1 = ((NBTTagCompound) nbtbase).get(this.a);

				if (nbtbase1 != null) {
					list.add(nbtbase1);
				}
			}

		}

		@Override
		public void a(NBTBase nbtbase, Supplier<NBTBase> supplier, List<NBTBase> list) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {
				NBTBase nbtbase1;

				if (nbttagcompound.hasKey(this.a)) {
					nbtbase1 = nbttagcompound.get(this.a);
				} else {
					nbtbase1 = supplier.get();
					nbttagcompound.set(this.a, nbtbase1);
				}

				list.add(nbtbase1);
			}

		}

		@Override
		public NBTBase a() {
			return new NBTTagCompound();
		}

		@Override
		public int a(NBTBase nbtbase, Supplier<NBTBase> supplier) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {
				NBTBase nbtbase1 = supplier.get();
				NBTBase nbtbase2 = nbttagcompound.set(this.a, nbtbase1);

				if (!nbtbase1.equals(nbtbase2)) {
					return 1;
				}
			}

			return 0;
		}

		@Override
		public int a(NBTBase nbtbase) {
			if (nbtbase instanceof NBTTagCompound nbttagcompound) {

				if (nbttagcompound.hasKey(this.a)) {
					nbttagcompound.remove(this.a);
					return 1;
				}
			}

			return 0;
		}
	}

	public static class h {

		private final String a;
		private final Object2IntMap<ArgumentNBTKey.i> b;
		private final ArgumentNBTKey.i[] c;

		public h(String s, ArgumentNBTKey.i[] aargumentnbtkey_i, Object2IntMap<ArgumentNBTKey.i> object2intmap) {
			this.a = s;
			this.c = aargumentnbtkey_i;
			this.b = object2intmap;
		}

		private static int a(List<NBTBase> list, Function<NBTBase, Integer> function) {
			return list.stream().map(function).reduce(0, Integer::sum);
		}

		public List<NBTBase> a(NBTBase nbtbase) throws CommandSyntaxException {
			List<NBTBase> list = Collections.singletonList(nbtbase);
			ArgumentNBTKey.i[] aargumentnbtkey_i = this.c;
			int i = aargumentnbtkey_i.length;

			for (int j = 0; j < i; ++j) {
				ArgumentNBTKey.i argumentnbtkey_i = aargumentnbtkey_i[j];

				list = argumentnbtkey_i.a(list);
				if (list.isEmpty()) {
					throw this.a(argumentnbtkey_i);
				}
			}

			return list;
		}

		public int b(NBTBase nbtbase) {
			List<NBTBase> list = Collections.singletonList(nbtbase);
			ArgumentNBTKey.i[] aargumentnbtkey_i = this.c;
			int i = aargumentnbtkey_i.length;

			for (int j = 0; j < i; ++j) {
				ArgumentNBTKey.i argumentnbtkey_i = aargumentnbtkey_i[j];

				list = argumentnbtkey_i.a(list);
				if (list.isEmpty()) {
					return 0;
				}
			}

			return list.size();
		}

		private List<NBTBase> d(NBTBase nbtbase) throws CommandSyntaxException {
			List<NBTBase> list = Collections.singletonList(nbtbase);

			for (int i = 0; i < this.c.length - 1; ++i) {
				ArgumentNBTKey.i argumentnbtkey_i = this.c[i];
				int j = i + 1;
				ArgumentNBTKey.i argumentnbtkey_i1 = this.c[j];

				Supplier<NBTBase> fn = argumentnbtkey_i1::a;
				list = argumentnbtkey_i.a(list, fn);
				if (list.isEmpty()) {
					throw this.a(argumentnbtkey_i);
				}
			}

			return list;
		}

		public List<NBTBase> a(NBTBase nbtbase, Supplier<NBTBase> supplier) throws CommandSyntaxException {
			List<NBTBase> list = this.d(nbtbase);
			ArgumentNBTKey.i argumentnbtkey_i = this.c[this.c.length - 1];

			return argumentnbtkey_i.a(list, supplier);
		}

		public int b(NBTBase nbtbase, Supplier<NBTBase> supplier) throws CommandSyntaxException {
			List<NBTBase> list = this.d(nbtbase);
			ArgumentNBTKey.i argumentnbtkey_i = this.c[this.c.length - 1];

			return a(list, (nbtbase1) -> argumentnbtkey_i.a(nbtbase1, supplier));
		}

		public int c(NBTBase nbtbase) {
			List<NBTBase> list = Collections.singletonList(nbtbase);

			for (int i = 0; i < this.c.length - 1; ++i) {
				list = this.c[i].a(list);
			}

			ArgumentNBTKey.i argumentnbtkey_i = this.c[this.c.length - 1];

			return a(list, argumentnbtkey_i::a);
		}

		private CommandSyntaxException a(ArgumentNBTKey.i argumentnbtkey_i) {
			int i = this.b.getInt(argumentnbtkey_i);

			return ArgumentNBTKey.b.create(this.a.substring(0, i));
		}

		public String toString() {
			return this.a;
		}
	}
}

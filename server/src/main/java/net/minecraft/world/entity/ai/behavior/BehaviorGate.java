package net.minecraft.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BehaviorGate<E extends EntityLiving> extends Behavior<E> {

    private final Set<MemoryModuleType<?>> b;
    private final BehaviorGate.Order c;
    private final BehaviorGate.Execution d;
    private final WeightedList<Behavior<? super E>> e = new WeightedList<>(false); protected final WeightedList<Behavior<? super E>> getList() { return this.e; } // Paper - don't use a clone // Tuinity - OBFHELPER

    public BehaviorGate(Map<MemoryModuleType<?>, MemoryStatus> map, Set<MemoryModuleType<?>> set, BehaviorGate.Order behaviorgate_order, BehaviorGate.Execution behaviorgate_execution, List<Pair<Behavior<? super E>, Integer>> list) {
        super(map);
        this.b = set;
        this.c = behaviorgate_order;
        this.d = behaviorgate_execution;
        list.forEach((pair) -> {
            this.e.a(pair.getFirst(), (Integer) pair.getSecond());
        });
    }

    @Override
    protected boolean b(WorldServer worldserver, E e0, long i) {
        // Tuinity start - remove streams
        List<WeightedList.a<Behavior<? super E>>> list = this.getList().getList();
        for (int index = 0, len = list.size(); index < len; ++index) {
            Behavior<? super E> behavior = list.get(index).getValue();
            if (behavior.getStatus() == Status.RUNNING && behavior.b(worldserver, e0, i)) { // copied from removed code, make sure to update
                return true;
            }
        }

        return false;
        // Tuinity end - remove streams
    }

    @Override
    protected boolean a(long i) {
        return false;
    }

    @Override
    protected void a(WorldServer worldserver, E e0, long i) {
        this.c.a(this.e);
        this.d.a(this.e, worldserver, e0, i);
    }

    @Override
    protected void d(WorldServer worldserver, E e0, long i) {
        // Tuinity start - remove streams
        List<WeightedList.a<Behavior<? super E>>> list = this.getList().getList();
        for (int index = 0, len = list.size(); index < len; ++index) {
            Behavior<? super E> behavior = list.get(index).getValue();
            if (behavior.getStatus() == Behavior.Status.RUNNING) {
                behavior.f(worldserver, e0, i); // copied from removed code, make sure to update
            }
        }
        // Tuinity end - remove streams
    }

    @Override
    protected void c(WorldServer worldserver, E e0, long i) {
        // Tuinity start - remove streams
        List<WeightedList.a<Behavior<? super E>>> list = this.getList().getList();
        for (int index = 0, len = list.size(); index < len; ++index) {
            Behavior<? super E> behavior = list.get(index).getValue();
            if (behavior.getStatus() == Behavior.Status.RUNNING) {
                behavior.g(worldserver, e0, i); // copied from removed code, make sure to update
            }
        }
        // Tuinity end - remove streams
        BehaviorController behaviorcontroller = e0.getBehaviorController();

        this.b.forEach(behaviorcontroller::removeMemory); // Paper - decomp fix
    }

    @Override
    public String toString() {
        Set<? extends Behavior<? super E>> set = this.e.c().filter((behavior) -> {
            return behavior.a() == Status.RUNNING;
        }).collect(Collectors.toSet());

        return "(" + this.getClass().getSimpleName() + "): " + set;
    }

    static enum Execution {

        RUN_ONE {
            @Override
            public <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i) {
                // Tuinity start - remove streams
                List<WeightedList.a<Behavior<? super E>>> list = weightedlist.getList();
                for (int index = 0, len = list.size(); index < len; ++index) {
                    Behavior<? super E> behavior = list.get(index).getValue();
                    if (behavior.getStatus() == Behavior.Status.STOPPED && behavior.e(worldserver, e0, i)) { // copied from removed code, make sure to update
                        break;
                    }
                }
                // Tuinity end - remove streams
            }
        },
        TRY_ALL {
            @Override
            public <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i) {
                // Tuinity start - remove streams
                List<WeightedList.a<Behavior<? super E>>> list = weightedlist.getList();
                for (int index = 0, len = list.size(); index < len; ++index) {
                    Behavior<? super E> behavior = list.get(index).getValue();
                    if (behavior.getStatus() == Behavior.Status.STOPPED) {
                        behavior.e(worldserver, e0, i); // copied from removed code, make sure to update
                    }
                }
                // Tuinity end - remove streams
            }
        };

        private Execution() {}

        public abstract <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i);
    }

    static enum Order {

        ORDERED((weightedlist) -> {
        }), SHUFFLED(WeightedList::a);

        private final Consumer<WeightedList<?>> c;

        private Order(Consumer<WeightedList<?>> consumer) { // Paper - decomp fix
            this.c = consumer;
        }

        public void a(WeightedList<?> weightedlist) {
            this.c.accept(weightedlist);
        }
    }
}

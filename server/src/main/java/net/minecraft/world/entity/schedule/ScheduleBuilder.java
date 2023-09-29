package net.minecraft.world.entity.schedule;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleBuilder {
	private final Schedule a;
	private final List<a_> b = Lists.newArrayList();

	public ScheduleBuilder(Schedule schedule) {
		this.a = schedule;
	}

	public ScheduleBuilder a(int i, Activity activity) {
		this.b.add(new a_(i, activity));
		return this;
	}

	public Schedule a() {
		var set = this.b.stream().map(ScheduleBuilder.a_::b).collect(Collectors.toSet());
		set.forEach(this.a::a);
		this.b.forEach((schedulebuilder_a) -> {
			Activity activity = schedulebuilder_a.b();

			this.a.c(activity).forEach((scheduleactivity) -> {
				scheduleactivity.a(schedulebuilder_a.a(), 0.0F);
			});
			this.a.b(activity).a(schedulebuilder_a.a(), 1.0F);
		});
		return this.a;
	}

	public static class a_ {

		private final int a;
		private final Activity b;

		public a_(int i, Activity activity) {
			this.a = i;
			this.b = activity;
		}

		public int a() {
			return this.a;
		}

		public Activity b() {
			return this.b;
		}
	}
}

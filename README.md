# Bayes Java Dota Challenge

This is the [task](TASK.md).

## Implementation Details
- `service` package represent domain model. `MatchService` is used by `MatchController`.

- `rest.model` package is moved to `service.model` package since it is considered as a part of domain model. `service.model` 
classes are used in both persistence and rest layers

- Queries corresponding to rest requests are implemented in `CombatLogEntryRepository`. JPA queries are implemented in
Spring repository interface.

- Error handling on the rest endpoint level is missing due to limited development time. For instance, when match id or 
hero name is missing, 404 Http code should be returned.

- `CombatLogParser` consists of `CombatLogLineParser`s. There are currently 4 types of line parsers: Hero Killed, Item
Purchase, Damage Done and Spell Cast parsers. When a new type of line parser needs to be implemented, it can be easily
implemented by extending `CombatLogLineParser` class.
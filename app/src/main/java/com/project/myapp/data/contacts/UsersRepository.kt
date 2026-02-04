package com.project.myapp.data.contacts

import com.github.javafaker.Faker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class UsersRepository @Inject constructor() : IUsersRepository {
    private var lastDeletedUser: User? = null
    private var lastDeletedIndex: Int? = null
    private var lastRestoredIndex: Int? = null

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    override val userList: StateFlow<List<User>> get() = _userList

    init {
        initializePreparedList()
    }

    private fun initializePreparedList() {
        val users = buildInitialUsersFromDifLists()
        addInitialUsers(users)
    }

    private fun addInitialUsers(users: List<User>) {
        _userList.update { current -> current + users }
    }

    /**
     * Saves user's information to one List.
     */

    private fun buildInitialUsersFromDifLists(): List<User> {
        val minLength = minOf(nameList.size, professionList.size, photoList.size)
        return List(minLength) { index ->
            User(id = index + 1, name = nameList[index], profession = professionList[index], photoUrl = photoList[index])
        }
    }

    /**
     * Adds a new user to _userList (for example using a button).
     */

    override fun addUser(
        name: String,
        profession: String,
    ) {
        val faker = Faker()

        _userList.update { current ->
            val nextId = (current.maxOfOrNull { it.id } ?: 1) + 1
            val finalProfession =
                profession.let {
                    it.ifEmpty { faker.job().title() }
                }
            val user = User(id = nextId, name = name, profession = finalProfession, photoUrl = randomAvatar())
            current + user
        }
    }

    private fun randomAvatar(): String {
        val isFemale = Random.nextBoolean()
        val genderPath = if (isFemale) "women" else "men"
        val id = Random.nextInt(1, 20)
        return "https://randomuser.me/api/portraits/$genderPath/$id.jpg"
    }

    override fun deleteUser(user: User) {
        _userList.update { current ->
            val index = current.indexOf(user)
            if (index == -1) return@update current
            lastDeletedUser = user
            lastDeletedIndex = index
            current.filterIndexed { i, _ -> i != index }
        }
    }

    override fun undoDeleteUser() {
        val user = lastDeletedUser ?: return
        val index = lastDeletedIndex ?: _userList.value.size

        val currentList = _userList.value.toMutableList()
        val safeIndex = if (index in 0..currentList.size) index else currentList.size
        currentList.add(safeIndex, user)
        _userList.value = currentList

        lastDeletedUser = null
        lastDeletedIndex = null

        lastRestoredIndex = safeIndex
    }

    override fun getLastRestoredIndex(): Int? {
        val index = lastRestoredIndex
        lastRestoredIndex = null
        return index
    }

    companion object {
        val nameList =
            listOf(
                "James Moriarty", "Ginny Weasley", "Robinson Crusoe", "Samwise Gamgee", "Arya Stark",
                "Peter Pan", "Clark Kent", "Forrest Gump", "Amy March", "Elizabet Bennet",
                "Jean Valjean", "Scarlet O'Hara",
            )
        val professionList =
            listOf(
                "Project manager", "Pilot", "Builder", "Chef", "Psychologist",
                "Flight attendant", "Journalist", "Writer", "Artist","Musician",
                "Firefighter", "Actress",
            )
        val photoList =
            listOf(
                "https://cdn.pixabay.com/photo/2018/11/08/23/52/man-3803551_1280.jpg",
                "https://cdn.pixabay.com/photo/2020/09/29/13/27/woman-5612838_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/11/21/12/42/beard-1845166_1280.jpg",
                "https://cdn.pixabay.com/photo/2019/12/23/08/15/orange-jacket-4714097_1280.jpg",
                "error url",
                "https://cdn.pixabay.com/photo/2016/11/29/05/11/adult-1867471_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/06/20/04/30/asian-man-1468032_1280.jpg",
                "https://cdn.pixabay.com/photo/2020/09/02/20/52/dock-5539524_1280.jpg",
                "https://cdn.pixabay.com/photo/2015/03/03/18/58/woman-657753_1280.jpg",
                "https://cdn.pixabay.com/photo/2023/03/16/15/00/woman-7856919_1280.jpg",
                "",
                "https://cdn.pixabay.com/photo/2022/04/30/14/04/woman-7165664_1280.jpg",
            )
    }
}

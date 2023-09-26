device="/dev/aesdchar"
target="beaglebone-yocto.local"
#target="localhost"
#target="192.168.1.7"
port=9000
cache="./tempfile"

function fill
{
    echo "Write1"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write2"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write3"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write4"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write5"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write6"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write7"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write8"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write9"  | nc ${target} ${port} -w 1 > /dev/null
    echo "Write10" | nc ${target} ${port} -w 1 > ${cache}
}

case "$1" in 
    fill_buffer)
    fill
    ;;

    write)
    echo $2 | nc ${target} ${port} -w 1 > ${cache}
    ;;

    read)
    cat ${cache}
    ;;

    seek)
    entry=$2
    offset=$3
    echo "AESDCHAR_IOCSEEKTO:${entry},${offset}" | nc ${target} ${port} -w 1 > ${cache}
    cat ${cache}
    ;;
    
    *)
    echo "Wrong arguments"
    exit 1
    ;;
esac

exit 0




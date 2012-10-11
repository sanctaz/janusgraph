package com.thinkaurelius.titan.graphdb.database.idassigner.placement;

import com.thinkaurelius.titan.graphdb.vertices.InternalTitanVertex;

import java.util.Map;

/**
 * (c) Matthias Broecheler (me@matthiasb.com)
 */

public interface IDPlacementStrategy {

    /**
     * Individually assigns an id to the given vertex or relation.
     * 
     * @param vertex Vertex or relation to assign id to.
     * @return
     */
    public long getPartition(InternalTitanVertex vertex);

    /**
     * Bulk assignment of idAuthorities to vertices.
     *
     * It is expected that the passed in map contains the partition assignment after this method
     * returns. Any initial values in the map are meaningless and to be ignored.
     * 
     * This is an optional operation. Check with {@link #supportsBulkPlacement()} first.
     *
     * @param vertices Map containing all vertices and their partition placement.
     */
    public void getPartitions(Map<InternalTitanVertex,PartitionAssignment> vertices);

    /**
     * Whether this placement strategy supports bulk placement.
     * If not, then {@link #getPartitions(java.util.Map)} will throw {@link UnsupportedOperationException}
     *
     * @return
     */
    public boolean supportsBulkPlacement();

    /**
     * If Titan is embedded, this method is used to indicate to the placement strategy which
     * part of the id space is hosted locally.
     *
     * This method can be called at any time while Titan is running. It is typically called right
     * after construction and when the id space is redistributed.
     *
     * Note, that the portion of the locally hosted keyspace may "wrap around", meaning that the lowerID>upperID
     * which describes the id block starting at lowerID (inclusive) than wrapping around idLimit (exclusive) to
     * upperID (exclusive).
     *
     * @param lowerID lower bound of the locally hosted id space (inclusive)
     * @param upperID upper bound of the locally hosted id space (exclusive)
     * @param idLimit any valid partition id must be smaller than this limit. Used to compute wrap arounds.
     */
    public void setLocalPartitionBounds(int lowerID, int upperID, int idLimit);

    /**
     * Called when there are no more idAuthorities left in the given partition. It is expected that the
     * placement strategy will no longer use said partition in its placement suggestions.
     *
     * @param partitionID Id of the partition that has been exhausted.
     */
    public void exhaustedPartition(int partitionID);

}

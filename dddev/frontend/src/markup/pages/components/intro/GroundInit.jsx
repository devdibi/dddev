import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';

import Select from 'markup/pages/components/common/Select';

import * as s from 'markup/styles/components/intro/GroundInit';
const GroundInit = () => {
  const user = useSelector((state) => state.user);
  const [repositories, setRepositories] = useState([]);
  const [repository, setRepository] = useState(null);

  useEffect(() => {
    eetch
      .repoList({ Authorization: user.accessToken })
      .then((res) => {
        setRepositories(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [user.accessToken]);

  return (
    <s.GroundWrapper>
      <s.Title>GroundInit</s.Title>
      <s.GroundDeck>
        <s.GroundCard>
          <Select label="리포지터리" list={repositories} select={setRepository} />
        </s.GroundCard>
        <s.GroundCard
          onClick={() => {
            console.log(repository);
          }}
        >
          3r23432
        </s.GroundCard>
      </s.GroundDeck>
    </s.GroundWrapper>
  );
};

export default GroundInit;
